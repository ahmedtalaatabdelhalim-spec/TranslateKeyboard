package com.translate.keyboard.translation

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GoogleTranslateApi {
    @POST("language/translate/v2")
    suspend fun translate(
        @Query("key") apiKey: String,
        @Body request: TranslateRequest
    ): Response<TranslateResponse>

    @POST("language/translate/v2/detect")
    suspend fun detect(
        @Query("key") apiKey: String,
        @Body request: DetectRequest
    ): Response<DetectResponse>
}

data class TranslateRequest(
    val q: List<String>,
    val target: String,
    val source: String? = null,
    val format: String = "text"
)

data class TranslateResponse(
    val data: TranslateData
)

data class TranslateData(
    val translations: List<TranslationItem>
)

data class TranslationItem(
    val translatedText: String,
    val detectedSourceLanguage: String? = null
)

data class DetectRequest(
    val q: List<String>
)

data class DetectResponse(
    val data: DetectData
)

data class DetectData(
    val detections: List<List<DetectionItem>>
)

data class DetectionItem(
    val language: String,
    val confidence: Double
)

class GoogleCloudTranslationProvider(private val apiKey: String) : TranslationProvider {

    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl("https://translation.googleapis.com/")
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(GoogleTranslateApi::class.java)

    override suspend fun translate(text: String, source: String, target: String): TranslationResult {
        if (apiKey.isEmpty() || apiKey == "YOUR_API_KEY") {
             // Fallback for local testing if key is not provided to prevent crash
             return TranslationResult(
                 translatedText = "[SIMULATED $target] $text",
                 sourceLang = source,
                 targetLang = target,
                 isSuccess = true
             )
        }

        return try {
            val response = api.translate(apiKey, TranslateRequest(listOf(text), target, if (source == "auto") null else source))
            if (response.isSuccessful) {
                val translated = response.body()?.data?.translations?.firstOrNull()?.translatedText ?: text
                TranslationResult(translated, source, target, true)
            } else {
                TranslationResult(text, source, target, false, response.errorBody()?.string())
            }
        } catch (e: Exception) {
            TranslationResult(text, source, target, false, e.localizedMessage)
        }
    }

    override suspend fun detectLanguage(text: String): String {
        if (apiKey.isEmpty() || apiKey == "YOUR_API_KEY") return "auto"
        return try {
            val response = api.detect(apiKey, DetectRequest(listOf(text)))
            if (response.isSuccessful) {
                response.body()?.data?.detections?.firstOrNull()?.firstOrNull()?.language ?: "en"
            } else "en"
        } catch (e: Exception) {
            "en"
        }
    }
}
