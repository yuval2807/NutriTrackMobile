package com.example.nutriTrack.utils

import android.util.Log
import com.example.nutriTrack.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

data class GeminiRequest(val contents: List<ContentItem>)
data class ContentItem(val parts: List<PartItem>)
data class PartItem(val text: String)

data class GeminiResponse(val candidates: List<Candidate>)
data class Candidate(val content: ContentResponse)
data class ContentResponse(val parts: List<PartResponse>)
data class PartResponse(val text: String)

var PROMPT = "As a nutrition expert, please share an interesting and lesser-known fact about nutrition that can help individuals make healthier dietary choices. Ensure the fact is accurate, concise, and engaging for a general audience"
interface GeminiApi {

    @POST("v1beta/models/gemini-1.5-pro:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

suspend fun generateAIFunFact(prompt: String = PROMPT): String {
    val apiKey =  BuildConfig.GEMINI_API_KEY
//        "AIzaSyDAN8VsZ4ltljYij4VPYcRegKYoKZsaQNA" // Replace with your API key
    Log.e("FunFact", "GEMINI_API_KEY: ${BuildConfig.GEMINI_API_KEY}", )

    val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val geminiApi = retrofit.create(GeminiApi::class.java)

    val request = GeminiRequest(
        contents = listOf(
            ContentItem(
                parts = listOf(
                    PartItem(text = prompt)
                )
            )
        )
    )

    return try {
        val response = geminiApi.generateContent(apiKey, request)
        response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No fact found"
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}