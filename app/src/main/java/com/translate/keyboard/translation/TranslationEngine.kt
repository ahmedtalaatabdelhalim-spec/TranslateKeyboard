package com.translate.keyboard.translation

import android.content.Context

class TranslationEngine(context: Context) {
    // API key should be injected or read from secure config
    // For build-readiness, we use a placeholder that the provider handles gracefully
    private val provider: TranslationProvider = GoogleCloudTranslationProvider("YOUR_API_KEY")

    suspend fun translateText(text: String, targetLang: String): String {
        if (text.isBlank()) return ""

        val sourceLang = detectLanguage(text)

        // Special handling for Franco
        if (sourceLang == "franco") {
            val arabicText = FrancoConverter.francoToArabic(text)
            if (targetLang == "ar") return arabicText
            // If target is English, translate the Arabic result
            val result = provider.translate(arabicText, "ar", targetLang)
            return if (result.isSuccess) result.translatedText else arabicText
        }

        if (targetLang == "franco" && sourceLang == "ar") {
            return FrancoConverter.arabicToFranco(text)
        }

        val result = provider.translate(text, sourceLang, targetLang)
        return if (result.isSuccess) result.translatedText else text
    }

    suspend fun detectLanguage(text: String): String {
        if (FrancoConverter.isFranco(text)) return "franco"
        if (text.any { it in '؀'..'ۿ' }) return "ar"
        return provider.detectLanguage(text)
    }
}
