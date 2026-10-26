package com.translate.keyboard.keyboard

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import com.translate.keyboard.emoji.EmojiRepository
import com.translate.keyboard.utils.ThemeColors

class EmojiKeyboardLayoutManager(
    private val context: Context,
    private val theme: ThemeColors,
    private val onEmojiClick: (String) -> Unit,
    private val onBackClick: () -> Unit
) {
    fun createLayout(): View {
        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(theme.backgroundColor)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                800 // Height for emoji panel
            )
        }

        // Toolbar for categories
        val toolbar = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(theme.toolbarBackgroundColor)
            setPadding(16, 8, 16, 8)
        }

        val btnBack = TextView(context).apply {
            text = "⬅"
            setTextColor(theme.textColor)
            setPadding(16, 8, 32, 8)
            setOnClickListener { onBackClick() }
        }
        toolbar.addView(btnBack)

        for (category in EmojiRepository.categories.take(5)) {
            val tv = TextView(context).apply {
                text = category.emojis.firstOrNull() ?: ""
                textSize = 20f
                setPadding(16, 8, 16, 8)
                setOnClickListener {
                    // Switch category logic can be added here
                }
            }
            toolbar.addView(tv)
        }

        val emojiList = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(8, 8, 8, 8)
        }

        // Simple row-based display for emojis instead of GridView for simplicity in this VM
        val allEmojis = EmojiRepository.categories.flatMap { it.emojis }.take(60)
        var currentRow: LinearLayout? = null

        allEmojis.forEachIndexed { index, emoji ->
            if (index % 8 == 0) {
                currentRow = LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER
                }
                emojiList.addView(currentRow)
            }

            val tv = TextView(context).apply {
                text = emoji
                textSize = 24f
                setPadding(16, 16, 16, 16)
                setOnClickListener { onEmojiClick(emoji) }
            }
            currentRow?.addView(tv)
        }

        root.addView(toolbar)
        root.addView(emojiList)

        return root
    }
}
