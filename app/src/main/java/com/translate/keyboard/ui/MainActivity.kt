package com.translate.keyboard.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.translate.keyboard.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val tvTitle = TextView(this).apply {
            text = "Welcome to Translate Keyboard 🌐"
            textSize = 24f
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val tvDesc = TextView(this).apply {
            text = "Instant System-Wide Translation Keyboard for Android."
            textSize = 16f
            setPadding(0, 16, 0, 32)
        }

        val btnEnable = Button(this).apply {
            text = "1. Enable Translate Keyboard"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
            }
        }

        val btnSelect = Button(this).apply {
            text = "2. Select as Default Keyboard"
            setOnClickListener {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showInputMethodPicker()
            }
        }

        val tvTestTitle = TextView(this).apply {
            text = "\nTest Your Keyboard Here:"
            textSize = 18f
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val etTest = EditText(this).apply {
            hint = "Type Arabic, Franco, or English..."
            minLines = 3
        }

        layout.addView(tvTitle)
        layout.addView(tvDesc)
        layout.addView(btnEnable)
        layout.addView(btnSelect)
        layout.addView(tvTestTitle)
        layout.addView(etTest)

        setContentView(layout)
    }
}
