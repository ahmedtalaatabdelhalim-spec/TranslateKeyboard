package com.translate.keyboard.utils

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("keyboard_settings", Context.MODE_PRIVATE)

    fun isHapticEnabled(): Boolean = prefs.getBoolean("haptic_feedback", true)
    fun setHapticEnabled(enabled: Boolean) = prefs.edit().putBoolean("haptic_feedback", enabled).apply()

    fun isSoundEnabled(): Boolean = prefs.getBoolean("key_sound", false)
    fun setSoundEnabled(enabled: Boolean) = prefs.edit().putBoolean("key_sound", enabled).apply()

    fun getSoundVolume(): Float = prefs.getFloat("sound_volume", 0.5f)
    fun setSoundVolume(volume: Float) = prefs.edit().putFloat("sound_volume", volume).apply()

    fun getTheme(): String = prefs.getString("theme", "system") ?: "system"
    fun setTheme(theme: String) = prefs.edit().putString("theme", theme).apply()

    fun isAutoCorrectionEnabled(): Boolean = prefs.getBoolean("auto_correction", true)
    fun setAutoCorrectionEnabled(enabled: Boolean) = prefs.edit().putBoolean("auto_correction", enabled).apply()

    fun isSuggestionsEnabled(): Boolean = prefs.getBoolean("suggestions", true)
    fun setSuggestionsEnabled(enabled: Boolean) = prefs.edit().putBoolean("suggestions", enabled).apply()

    fun isClipboardTranslationEnabled(): Boolean = prefs.getBoolean("clipboard_translation", true)
    fun setClipboardTranslationEnabled(enabled: Boolean) = prefs.edit().putBoolean("clipboard_translation", enabled).apply()

    fun isFrancoModeEnabled(): Boolean = prefs.getBoolean("franco_mode", false)
    fun setFrancoModeEnabled(enabled: Boolean) = prefs.edit().putBoolean("franco_mode", enabled).apply()

    fun isPopupPreviewEnabled(): Boolean = prefs.getBoolean("popup_preview", true)
    fun setPopupPreviewEnabled(enabled: Boolean) = prefs.edit().putBoolean("popup_preview", enabled).apply()
}
