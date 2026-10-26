package com.translate.keyboard.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import android.view.View

class HapticManager(context: Context, private val settings: SettingsManager) {
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun performClick(view: View) {
        if (!settings.isHapticEnabled()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(10)
        }
    }

    fun performLongPress(view: View) {
        if (!settings.isHapticEnabled()) return
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
}
