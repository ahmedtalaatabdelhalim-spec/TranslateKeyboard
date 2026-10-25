package com.translate.keyboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.translate.keyboard.keyboard.KeyboardLayoutManager
import com.translate.keyboard.translation.TranslationEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TranslateKeyboardService : InputMethodService(), KeyboardLayoutManager.KeyClickListener {

    private lateinit var appTranslationEngine: TranslationEngine
    private lateinit var appKeyboardManager: KeyboardLayoutManager
    private lateinit var appClipboardManager: ClipboardManager
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())

    private var currentLayout = "ar" // "ar", "en", "symbols"
    private var targetLang = "en"    // "en", "ar", "franco"
    private var isShifted = false

    private lateinit var rootContainer: LinearLayout
    private lateinit var toolbarContainer: LinearLayout
    private lateinit var clipboardCardContainer: LinearLayout
    private lateinit var keyboardContainer: LinearLayout

    private var lastCopiedText = ""

    override fun onCreate() {
        super.onCreate()
        appTranslationEngine = TranslationEngine(applicationContext)
        appKeyboardManager = KeyboardLayoutManager(applicationContext)
        appKeyboardManager.setKeyClickListener(this)
        appClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreateInputView(): View {
        rootContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(android.graphics.Color.parseColor("#121212"))
        }

        setupToolbar()
        setupClipboardCard()

        keyboardContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(appKeyboardManager.createLayout(currentLayout, isShifted))
        }

        rootContainer.addView(toolbarContainer)
        rootContainer.addView(clipboardCardContainer)
        rootContainer.addView(keyboardContainer)

        checkClipboardContent()
        return rootContainer
    }

    private fun setupToolbar() {
        toolbarContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 12, 16, 12)
            setBackgroundColor(android.graphics.Color.parseColor("#1F1F1F"))
        }

        val btnTranslateAction = TextView(this).apply {
            text = "🌐 Translate"
            setTextColor(android.graphics.Color.WHITE)
            textSize = 14f
            setPadding(16, 8, 16, 8)
            setOnClickListener {
                performInstantTranslation()
            }
        }

        val btnClipboardAction = TextView(this).apply {
            text = "📋 Clipboard"
            setTextColor(android.graphics.Color.WHITE)
            textSize = 14f
            setPadding(16, 8, 16, 8)
            setOnClickListener {
                toggleClipboardCardVisibility()
            }
        }

        toolbarContainer.addView(btnTranslateAction)
        toolbarContainer.addView(btnClipboardAction)
    }

    private fun setupClipboardCard() {
        clipboardCardContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            visibility = View.GONE
            setPadding(16, 16, 16, 16)
            setBackgroundColor(android.graphics.Color.parseColor("#2C2C2C"))
        }
    }

    private fun checkClipboardContent() {
        try {
            val clipData = appClipboardManager.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val text = clipData.getItemAt(0).text?.toString() ?: ""
                if (text.isNotBlank() && text != lastCopiedText) {
                    lastCopiedText = text
                    showClipboardCard(text)
                }
            }
        } catch (e: Exception) {
            // Handle security exceptions gracefully
        }
    }

    private fun showClipboardCard(copiedText: String) {
        clipboardCardContainer.removeAllViews()
        clipboardCardContainer.visibility = View.VISIBLE

        val tvTitle = TextView(this).apply {
            text = "📋 Copied Text Detected:"
            setTextColor(android.graphics.Color.parseColor("#BB86FC"))
            textSize = 12f
        }

        val tvContent = TextView(this).apply {
            text = copiedText
            setTextColor(android.graphics.Color.WHITE)
            textSize = 14f
            setPadding(0, 4, 0, 8)
        }

        val actionRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val btnTranslateClip = TextView(this).apply {
            text = "[ Translate ]"
            setTextColor(android.graphics.Color.parseColor("#03DAC5"))
            setPadding(0, 4, 16, 4)
            setOnClickListener {
                serviceScope.launch {
                    val translated = appTranslationEngine.translateText(copiedText, targetLang)
                    tvContent.text = "$copiedText\n➔ $translated"
                }
            }
        }

        val btnInsertClip = TextView(this).apply {
            text = "[ Insert ]"
            setTextColor(android.graphics.Color.parseColor("#03DAC5"))
            setPadding(0, 4, 16, 4)
            setOnClickListener {
                currentInputConnection?.commitText(copiedText, 1)
                clipboardCardContainer.visibility = View.GONE
            }
        }


        val btnClose = TextView(this).apply {
            text = "[ Close ]"
            setTextColor(android.graphics.Color.GRAY)
            setPadding(0, 4, 0, 4)
            setOnClickListener {
                clipboardCardContainer.visibility = View.GONE
            }
        }

        actionRow.addView(btnTranslateClip)
        actionRow.addView(btnInsertClip)
        actionRow.addView(btnClose)

        clipboardCardContainer.addView(tvTitle)
        clipboardCardContainer.addView(tvContent)
        clipboardCardContainer.addView(actionRow)
    }

    private fun toggleClipboardCardVisibility() {
        if (clipboardCardContainer.visibility == View.VISIBLE) {
            clipboardCardContainer.visibility = View.GONE
        } else {
            checkClipboardContent()
            if (clipboardCardContainer.childCount == 0) {
                Toast.makeText(this, "No recent clipboard text", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onKeyClick(text: String) {
        performHaptic()
        currentInputConnection?.commitText(text, 1)
    }

    override fun onDeleteClick() {
        performHaptic()
        val ic = currentInputConnection ?: return
        val selectedText = ic.getSelectedText(0)
        if (!selectedText.isNullOrEmpty()) {
            ic.commitText("", 1)
        } else {
            ic.deleteSurroundingText(1, 0)
        }
    }

    override fun onSpaceClick() {
        performHaptic()
        currentInputConnection?.commitText(" ", 1)
    }

    override fun onEnterClick() {
        performHaptic()
        val ic = currentInputConnection ?: return
        val editorInfo = currentInputEditorInfo
        val action = editorInfo?.imeOptions ?: 0
        if (action and android.view.inputmethod.EditorInfo.IME_MASK_ACTION != android.view.inputmethod.EditorInfo.IME_ACTION_NONE) {
            ic.performEditorAction(action and android.view.inputmethod.EditorInfo.IME_MASK_ACTION)
        } else {
            ic.commitText("\n", 1)
        }
    }

    override fun onShiftClick() {
        isShifted = !isShifted
        refreshKeyboard()
    }

    override fun onLanguageClick() {
        currentLayout = when (currentLayout) {
            "ar" -> { targetLang = "ar"; "en" }
            "en" -> { targetLang = "en"; "ar" }
            else -> "ar"
        }
        refreshKeyboard()
        Toast.makeText(this, "Layout: $currentLayout", Toast.LENGTH_SHORT).show()
    }

    override fun onEmojiClick() {
        Toast.makeText(this, "Emoji Panel", Toast.LENGTH_SHORT).show()
    }

    override fun onLongEnterClick() {
        performInstantTranslation()
    }

    private fun performInstantTranslation() {
        val ic = currentInputConnection ?: return
        val textBefore = ic.getTextBeforeCursor(1000, 0)?.toString() ?: ""
        if (textBefore.isBlank()) {
            Toast.makeText(this, "No text to translate", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Translating...", Toast.LENGTH_SHORT).show()

        serviceScope.launch {
            try {
                val translated = withContext(Dispatchers.IO) {
                    appTranslationEngine.translateText(textBefore, targetLang)
                }
                ic.deleteSurroundingText(textBefore.length, 0)
                ic.commitText(translated, 1)
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Translation failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun refreshKeyboard() {
        keyboardContainer.removeAllViews()
        keyboardContainer.addView(appKeyboardManager.createLayout(currentLayout, isShifted))
    }

    private fun performHaptic() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(15)
        }
    }
}
