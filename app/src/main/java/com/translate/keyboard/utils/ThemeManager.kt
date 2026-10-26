package com.translate.keyboard.utils

import android.graphics.Color

data class ThemeColors(
    val backgroundColor: Int,
    val keyBackgroundColor: Int,
    val specialKeyBackgroundColor: Int,
    val enterKeyBackgroundColor: Int,
    val textColor: Int,
    val toolbarBackgroundColor: Int
)

object ThemeManager {
    fun getThemeColors(themeName: String): ThemeColors {
        return when (themeName) {
            "light" -> ThemeColors(
                backgroundColor = Color.parseColor("#F5F5F5"),
                keyBackgroundColor = Color.parseColor("#FFFFFF"),
                specialKeyBackgroundColor = Color.parseColor("#E0E0E0"),
                enterKeyBackgroundColor = Color.parseColor("#6200EE"),
                textColor = Color.parseColor("#000000"),
                toolbarBackgroundColor = Color.parseColor("#EEEEEE")
            )
            "dark" -> ThemeColors(
                backgroundColor = Color.parseColor("#121212"),
                keyBackgroundColor = Color.parseColor("#2A2A2A"),
                specialKeyBackgroundColor = Color.parseColor("#3A3A3A"),
                enterKeyBackgroundColor = Color.parseColor("#6200EE"),
                textColor = Color.parseColor("#FFFFFF"),
                toolbarBackgroundColor = Color.parseColor("#1F1F1F")
            )
            "material_you" -> ThemeColors(
                backgroundColor = Color.parseColor("#1C1B1F"),
                keyBackgroundColor = Color.parseColor("#2B2930"),
                specialKeyBackgroundColor = Color.parseColor("#4A4458"),
                enterKeyBackgroundColor = Color.parseColor("#D0BCFF"),
                textColor = Color.parseColor("#E6E1E5"),
                toolbarBackgroundColor = Color.parseColor("#25232A")
            )
            else -> getThemeColors("dark")
        }
    }
}
