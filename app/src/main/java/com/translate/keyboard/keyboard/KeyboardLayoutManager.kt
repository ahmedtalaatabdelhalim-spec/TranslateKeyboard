package com.translate.keyboard.keyboard

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.translate.keyboard.utils.SettingsManager
import com.translate.keyboard.utils.ThemeColors

class KeyboardLayoutManager(
    private val context: Context,
    private val settings: SettingsManager,
    private val theme: ThemeColors
) {

    interface KeyClickListener {
        fun onKeyClick(text: String)
        fun onDeleteClick()
        fun onSpaceClick()
        fun onEnterClick()
        fun onShiftClick()
        fun onLanguageClick()
        fun onEmojiClick()
        fun onLongEnterClick()
    }

    private var listener: KeyClickListener? = null
    fun setKeyClickListener(l: KeyClickListener) { listener = l }

    private var previewPopup: PopupWindow? = null
    private var previewTextView: TextView? = null

    init {
        setupPreviewPopup()
    }

    private fun setupPreviewPopup() {
        previewTextView = TextView(context).apply {
            textSize = 28f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setBackgroundDrawable(GradientDrawable().apply {
                setColor(Color.parseColor("#333333"))
                cornerRadius = 16f
            })
            setPadding(24, 12, 24, 12)
        }
        previewPopup = PopupWindow(previewTextView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            isTouchable = false
        }
    }

    private fun showPreview(anchor: View, text: String) {
        if (!settings.isPopupPreviewEnabled() || text.length > 2) return
        previewTextView?.text = text
        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        previewPopup?.showAtLocation(
            anchor,
            Gravity.NO_GRAVITY,
            location[0] + (anchor.width / 4),
            location[1] - 140
        )
    }

    private fun dismissPreview() {
        if (previewPopup?.isShowing == true) {
            previewPopup?.dismiss()
        }
    }

    fun createLayout(type: String, isShifted: Boolean = false): View {
        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(4, 6, 4, 6)
            setBackgroundColor(theme.backgroundColor)
        }

        when (type) {
            "ar" -> {
                // Row 1: Numbers
                root.addView(createRow("١٢٣٤٥٦٧٨٩٠"))
                // Row 2: Letters
                root.addView(createRow("ضصثقفغعهخحج"))
                // Row 3: Letters
                root.addView(createRow("شسيبلاتنمكط"))
                // Row 4: Letters + Backspace on the right
                root.addView(createRowWithBackspace("ذئؤرىةوزظد"))
                // Row 5: Bottom functional row
                root.addView(createArabicBottomRow())
            }
            "en" -> {
                val r1 = if (isShifted) "QWERTYUIOP" else "qwertyuiop"
                val r2 = if (isShifted) "ASDFGHJKL" else "asdfghjkl"
                val r3 = if (isShifted) "ZXCVBNM" else "zxcvbnm"

                root.addView(createRow(r1))
                root.addView(createRow(r2))
                root.addView(createRowWithSpecial(r3, "⇧", "⌫"))
                root.addView(createStandardBottomRow())
            }
            "symbols" -> {
                root.addView(createRow("1234567890"))
                root.addView(createRow("@#$%&*-+()"))
                root.addView(createRowWithSpecial("!\"':;/?~_", "ABC", "⌫"))
                root.addView(createStandardBottomRow())
            }
            else -> {
                root.addView(createRow("qwertyuiop"))
                root.addView(createRow("asdfghjkl"))
                root.addView(createRow("zxcvbnm"))
                root.addView(createStandardBottomRow())
            }
        }

        return root
    }

    private fun createRow(keys: String): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            for (ch in keys) {
                addView(createKeyButton(ch.toString()))
            }
        }
    }

    private fun createRowWithBackspace(keys: String): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            for (ch in keys) {
                addView(createKeyButton(ch.toString()))
            }
            // Backspace on the far right
            addView(createSpecialButton("⌫", 1.2f) { listener?.onDeleteClick() })
        }
    }

    private fun createRowWithSpecial(keys: String, leftKey: String, rightKey: String): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addView(createSpecialButton(leftKey, 1.2f) {
                if (leftKey == "⇧") listener?.onShiftClick()
                else if (leftKey == "ABC") listener?.onLanguageClick()
            })
            for (ch in keys) {
                addView(createKeyButton(ch.toString()))
            }
            addView(createSpecialButton(rightKey, 1.2f) {
                if (rightKey == "⌫") listener?.onDeleteClick()
            })
        }
    }

    private fun createArabicBottomRow(): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // Numbers / Symbols switch
            addView(createSpecialButton("؟١٢٣", 1.1f) { listener?.onLanguageClick() })
            // Emoji
            addView(createSpecialButton("😀", 0.9f) { listener?.onEmojiClick() })
            // Microphone
            addView(createSpecialButton("🎙", 0.9f) { /* voice */ })
            // Left Arrow
            addView(createSpecialButton("‹", 0.7f) { listener?.onKeyClick("‹") })

            // Spacebar labeled "العربية"
            addView(Button(context).apply {
                text = "العربية"
                layoutParams = LinearLayout.LayoutParams(0, 135, 3.5f).apply { setMargins(3, 4, 3, 4) }
                background = createBackground(theme.keyBackgroundColor)
                setTextColor(theme.textColor)
                textSize = 16f
                isAllCaps = false
                setOnClickListener { listener?.onSpaceClick() }
            })

            // Right Arrow (used for instant translation on long press)
            addView(createSpecialButton("›", 0.7f, true) { listener?.onKeyClick("›") })

            // Enter Button
            addView(createSpecialButton("↵", 1.2f, true) { listener?.onEnterClick() })
        }
    }

    private fun createStandardBottomRow(): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            addView(createSpecialButton("?123", 1.2f) { listener?.onLanguageClick() })
            addView(createSpecialButton("🌐", 1.0f) { listener?.onLanguageClick() })
            addView(createSpecialButton("😀", 1.0f) { listener?.onEmojiClick() })

            addView(Button(context).apply {
                text = "Space"
                layoutParams = LinearLayout.LayoutParams(0, 135, 4.0f).apply { setMargins(3, 4, 3, 4) }
                background = createBackground(theme.keyBackgroundColor)
                setTextColor(theme.textColor)
                textSize = 16f
                isAllCaps = false
                setOnClickListener { listener?.onSpaceClick() }
            })

            addView(createSpecialButton("↵", 1.2f, true) { listener?.onEnterClick() })
        }
    }

    private fun createKeyButton(text: String): View {
        return Button(context).apply {
            this.text = text
            layoutParams = LinearLayout.LayoutParams(0, 135, 1.0f).apply { setMargins(3, 4, 3, 4) }
            background = createBackground(theme.keyBackgroundColor)
            setTextColor(theme.textColor)
            textSize = 19f
            setPadding(0, 0, 0, 0)
            isAllCaps = false
            setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        showPreview(v, text)
                    }
                    MotionEvent.ACTION_UP -> {
                        dismissPreview()
                        listener?.onKeyClick(text)
                        v.performClick()
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        dismissPreview()
                    }
                }
                true
            }
        }
    }

    private fun createSpecialButton(text: String, weight: Float, isEnterOrAction: Boolean = false, action: () -> Unit): View {
        return Button(context).apply {
            this.text = text
            layoutParams = LinearLayout.LayoutParams(0, 135, weight).apply { setMargins(3, 4, 3, 4) }
            background = createBackground(if (isEnterOrAction) theme.enterKeyBackgroundColor else theme.specialKeyBackgroundColor)
            setTextColor(theme.textColor)
            textSize = 16f
            setPadding(0, 0, 0, 0)
            isAllCaps = false
            setOnClickListener { action() }
            if (isEnterOrAction) {
                setOnLongClickListener {
                    listener?.onLongEnterClick()
                    true
                }
            }
        }
    }

    private fun createBackground(colorInt: Int): GradientDrawable {
        return GradientDrawable().apply {
            setColor(colorInt)
            cornerRadius = 18f
        }
    }
}
