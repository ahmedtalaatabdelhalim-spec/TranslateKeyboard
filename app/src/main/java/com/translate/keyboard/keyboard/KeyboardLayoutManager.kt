package com.translate.keyboard.keyboard

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class KeyboardLayoutManager(private val context: Context) {

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

    fun createLayout(type: String, isShifted: Boolean = false): View {
        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(4, 4, 4, 4)
            setBackgroundColor(Color.parseColor("#121212")) // Dark Theme Base
        }

        val keys = when (type) {
            "ar" -> listOf(
                "ضصثقفاغعهخحجد",
                "شسيبلاتنمكط",
                "ئءؤرلاىةوزظ"
            )
            "en" -> {
                val row1 = if (isShifted) "QWERTYUIOP" else "qwertyuiop"
                val row2 = if (isShifted) "ASDFGHJKL" else "asdfghjkl"
                val row3 = if (isShifted) "ZXCVBNM" else "zxcvbnm"
                listOf(row1, row2, row3)
            }
            "symbols" -> listOf(
                "1234567890",
                "@#$%&*-+()",
                "!\"':;/?~_ "
            )
            else -> listOf("qwertyuiop", "asdfghjkl", "zxcvbnm")
        }

        for (i in keys.indices) {
            val row = createRow(keys[i], type, i == 2, isShifted)
            root.addView(row)
        }

        root.addView(createBottomRow(type))

        return root
    }

    private fun createRow(keys: String, type: String, isThirdRow: Boolean, isShifted: Boolean): LinearLayout {
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        if (isThirdRow && type == "en") {
            row.addView(createSpecialKey("⇧", 1.2f) { listener?.onShiftClick() })
        }

        for (ch in keys) {
            row.addView(createKey(ch.toString()))
        }

        if (isThirdRow) {
            row.addView(createSpecialKey("⌫", 1.2f) { listener?.onDeleteClick() })
        }

        return row
    }

    private fun createBottomRow(type: String): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            addView(createSpecialKey("?123", 1.5f) { /* Switch to symbols */ })
            addView(createSpecialKey("🌐", 1.2f) { listener?.onLanguageClick() })
            addView(createSpecialKey("😀", 1.2f) { listener?.onEmojiClick() })

            addView(Button(context).apply {
                text = "Space"
                layoutParams = LinearLayout.LayoutParams(0, 140, 4f).apply { setMargins(4, 8, 4, 8) }
                background = createKeyBackground("#333333")
                setTextColor(Color.WHITE)
                isAllCaps = false
                setOnClickListener { listener?.onSpaceClick() }
            })

            addView(createSpecialKey("↵", 1.5f, true) { listener?.onEnterClick() })
        }
    }

    private fun createKey(txt: String): View {
        val btn = Button(context).apply {
            text = txt
            layoutParams = LinearLayout.LayoutParams(0, 140, 1f).apply { setMargins(4, 6, 4, 6) }
            background = createKeyBackground("#2A2A2A")
            setTextColor(Color.WHITE)
            textSize = 20f
            setPadding(0, 0, 0, 0)
            isAllCaps = false
            setOnClickListener { listener?.onKeyClick(txt) }
        }
        return btn
    }

    private fun createSpecialKey(txt: String, weight: Float, isEnter: Boolean = false, action: () -> Unit): View {
        return Button(context).apply {
            text = txt
            layoutParams = LinearLayout.LayoutParams(0, 140, weight).apply { setMargins(4, 8, 4, 8) }
            background = createKeyBackground(if (isEnter) "#6200EE" else "#3A3A3A")
            setTextColor(Color.WHITE)
            textSize = 18f
            setOnClickListener { action() }
            if (isEnter) {
                setOnLongClickListener {
                    listener?.onLongEnterClick()
                    true
                }
            }
        }
    }

    private fun createKeyBackground(color: String): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor(color))
            cornerRadius = 16f
        }
    }
}
