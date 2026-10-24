package com.translate.keyboard

import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.TextUtils
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.translate.keyboard.clipboard.ClipboardManagerHelper
import com.translate.keyboard.emoji.EmojiRepository
import com.translate.keyboard.translation.FrancoConverter
import com.translate.keyboard.translation.TranslationEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TranslateKeyboardService : InputMethodService() {

    private lateinit var translationEngine: TranslationEngine
    private lateinit var clipboardHelper: ClipboardManagerHelper
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())

    private var currentLayout = "ar" // "ar", "en", "franco"
    private var targetLang = "en"   // "en", "ar", "franco", "fr", "de", "es"
    private var isShifted = false

    private lateinit var keyboardContainer: LinearLayout
    private lateinit var toolbar: LinearLayout
    private lateinit var keyboardView: View
    private lateinit var emojiView: ScrollView
    private lateinit var clipboardView: ScrollView
    private lateinit var translationBar: LinearLayout

    private lateinit var btnTranslate: ImageButton
    private lateinit var btnLanguage: Button
    private lateinit var btnClipboard: ImageButton
    private lateinit var btnEmoji: ImageButton
    private lateinit var btnVoice: ImageButton
    private lateinit var btnSettings: ImageButton
    private lateinit var tvTranslationPreview: TextView

    override fun onCreate() {
        super.onCreate()
        translationEngine = TranslationEngine(applicationContext)
        clipboardHelper = ClipboardManagerHelper(applicationContext)
    }

    override fun onCreateInputView(): View {
        keyboardContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(0xFF1E1E2E.toInt()) // Dark Material Theme default
        }

        setupToolbar()
        setupTranslationBar()
        setupMainKeyboardLayout()

        keyboardContainer.addView(toolbar)
        keyboardContainer.addView(translationBar)
        keyboardContainer.addView(keyboardView)

        return keyboardContainer
    }

    private fun setupToolbar() {
        toolbar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(12, 8, 12, 8)
            setBackgroundColor(0xFF2A2A3C.toInt())
        }

        btnLanguage = Button(this).apply {
            text = "AR ⇄ EN"
            textSize = 12f
            setOnClickListener {
                switchLanguage()
            }
        }

        btnTranslate = ImageButton(this).apply {
            setImageResource(android.R.drawable.ic_menu_send)
            setOnClickListener {
                performInstantTranslation()
            }
        }

        btnClipboard = ImageButton(this).apply {
            setImageResource(android.R.drawable.ic_menu_save)
            setOnClickListener {
                toggleClipboardView()
            }
        }

        btnEmoji = ImageButton(this).apply {
            setImageResource(android.R.drawable.ic_menu_gallery)
            setOnClickListener {
                toggleEmojiView()
            }
        }

        btnVoice = ImageButton(this).apply {
            setImageResource(android.R.drawable.ic_btn_speak_now)
            setOnClickListener {
                Toast.makeText(applicationContext, "Listening...", Toast.LENGTH_SHORT).show()
            }
        }

        toolbar.addView(btnLanguage)
        toolbar.addView(btnTranslate)
        toolbar.addView(btnClipboard)
        toolbar.addView(btnEmoji)
        toolbar.addView(btnVoice)
    }

    private fun setupTranslationBar() {
        translationBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            visibility = View.GONE
            setPadding(16, 8, 16, 8)
            setBackgroundColor(0xFF3B3B52.toInt())
        }

        tvTranslationPreview = TextView(this).apply {
            text = "Translation Preview..."
            setTextColor(0xFFFFFFFF.toInt())
            textSize = 14f
        }
        translationBar.addView(tvTranslationPreview)
    }

    private fun setupMainKeyboardLayout() {
        keyboardView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(8, 8, 8, 8)
        }

        // Add key rows programmatically (QWERTY or Arabic)
        val row1 = createKeyRow(if (currentLayout == "ar") "ضصثقفاغعهخحجد" else "qwertyuiop")
        val row2 = createKeyRow(if (currentLayout == "ar") "شسيبلاتنمكط" else "asdfghjkl")
        val row3 = createKeyRow(if (currentLayout == "ar") "ئءؤرلاىةوزظ" else "zxcvbnm")

        val bottomRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val btnSpace = Button(this).apply {
            text = "Space ($currentLayout)"
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
            setOnClickListener {
                commitText(" ")
            }
        }

        val btnEnter = Button(this).apply {
            text = "↵"
            setOnClickListener {
                sendKeyChar('\n')
            }
            setOnLongClickListener {
                performInstantTranslation()
                true
            }
        }

        val btnDelete = Button(this).apply {
            text = "⌫"
            setOnClickListener {
                currentInputConnection?.deleteSurroundingText(1, 0)
            }
        }

        bottomRow.addView(btnSpace)
        bottomRow.addView(btnDelete)
        bottomRow.addView(btnEnter)

        (keyboardView as LinearLayout).addView(row1)
        (keyboardView as LinearLayout).addView(row2)
        (keyboardView as LinearLayout).addView(row3)
        (keyboardView as LinearLayout).addView(bottomRow)
    }

    private fun createKeyRow(keys: String): LinearLayout {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        for (ch in keys) {
            val btn = Button(this).apply {
                text = ch.toString()
                textSize = 18f
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
                setOnClickListener {
                    performHapticFeedback()
                    commitText(text.toString())
                }
            }
            row.addView(btn)
        }
        return row
    }

    private fun commitText(text: String) {
        currentInputConnection?.commitText(text, 1)
    }

    private fun performInstantTranslation() {
        val ic = currentInputConnection ?: return
        val textBefore = ic.getTextBeforeCursor(500, 0)?.toString() ?: ""
        if (textBefore.isBlank()) return

        val detected = translationEngine.detectLanguage(textBefore)
        val target = if (detected == "ar") "en" else "ar"

        serviceScope.launch {
            val translated = translationEngine.translate(textBefore, detected, target)
            ic.deleteSurroundingText(textBefore.length, 0)
            ic.commitText(translated, 1)
            Toast.makeText(applicationContext, "Translated ($detected ➔ $target)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun switchLanguage() {
        currentLayout = when (currentLayout) {
            "ar" -> "en"
            "en" -> "franco"
            else -> "ar"
        }
        btnLanguage.text = currentLayout.uppercase()
        keyboardContainer.removeView(keyboardView)
        setupMainKeyboardLayout()
        keyboardContainer.addView(keyboardView)
    }

    private fun toggleEmojiView() {
        Toast.makeText(applicationContext, "Emoji Picker Opened", Toast.LENGTH_SHORT).show()
    }

    private fun toggleClipboardView() {
        Toast.makeText(applicationContext, "Clipboard History Opened", Toast.LENGTH_SHORT).show()
    }

    private fun performHapticFeedback() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(20)
        }
    }
}
