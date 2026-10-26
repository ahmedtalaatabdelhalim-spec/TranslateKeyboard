package com.translate.keyboard.utils

import android.content.Context
import java.util.Locale

class SuggestionEngine(private val context: Context) {

    fun getSuggestions(currentText: String, language: String): List<String> {
        if (currentText.isBlank()) return emptyList()

        val lastWord = currentText.split(" ").lastOrNull()?.lowercase(Locale.ROOT) ?: return emptyList()

        return when (language) {
            "ar" -> getArabicSuggestions(lastWord)
            "en" -> getEnglishSuggestions(lastWord)
            else -> emptyList()
        }
    }

    private fun getArabicSuggestions(word: String): List<String> {
        val dict = listOf("السلام", "عليكم", "صباح", "الخير", "مساء", "الحمد", "لله", "شكرا", "إن", "شاء", "الله")
        return dict.filter { it.startsWith(word) }.take(3)
    }

    private fun getEnglishSuggestions(word: String): List<String> {
        val dict = listOf("the", "be", "to", "of", "and", "a", "in", "that", "have", "I", "it", "for", "not", "on", "with", "he", "as", "you", "do", "at")
        return dict.filter { it.startsWith(word) }.take(3)
    }
}
