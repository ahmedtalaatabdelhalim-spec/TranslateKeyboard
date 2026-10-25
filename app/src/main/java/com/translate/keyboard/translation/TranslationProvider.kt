package com.translate.keyboard.translation

interface TranslationProvider {
    suspend fun translate(text: String, source: String, target: String): TranslationResult
    suspend fun detectLanguage(text: String): String
}

data class TranslationResult(
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String,
    val isSuccess: Boolean,
    val errorMessage: String? = null
)
