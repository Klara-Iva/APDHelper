package com.example.apdhelper.api

import android.util.Log
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object OpenAIService {

    private const val BASE_URL = "https://api.openai.com/v1/"
    private const val API_KEY =""
           private val retrofit by lazy {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor { chain ->
                val request =
                    chain.request().newBuilder().addHeader("Authorization", "Bearer $API_KEY")
                        .build()
                chain.proceed(request)
            }.addInterceptor(logger).build()

        Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private val api = retrofit.create(OpenAIInterface::class.java)

    suspend fun getRecommendations(prompt: String): String {
        val request = ChatRequest(
            model = "gpt-3.5-turbo-1106", messages = listOf(
                ChatMessage(
                    "system",
                    "You are a mental health assistant that gives calm, evidence-based and personalized advice."
                ), ChatMessage("user", prompt)
            )
        )

        var attempt = 0
        var lastException: Exception? = null

        while (attempt < 1) {
            try {
                Log.d("OpenAI", "Sending prompt: $prompt")
                val response = api.getChatCompletion(request)
                val result = response.choices.firstOrNull()?.message?.content
                Log.d("OpenAI", "OpenAI response: $result")
                return result ?: "No helpful recommendation received from the assistant."
            } catch (e: Exception) {
                Log.e("OpenAI", "API error: ${e.message}", e)
                if (e.message?.contains("429") == true) {
                    val backoff = 1000L * (attempt + 1)
                    delay(backoff)
                    attempt++
                    lastException = e
                } else {
                    throw e
                }
            }
        }
        Log.e("OpenAI", "Final failure after retries", lastException)
        return "We're currently unable to retrieve recommendations. Please try again later."
    }
}

interface OpenAIInterface {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun getChatCompletion(@Body request: ChatRequest): ChatResponse
}

data class ChatRequest(
    val model: String, val messages: List<ChatMessage>
)

data class ChatMessage(
    val role: String, val content: String
)

data class ChatResponse(
    val choices: List<ChatChoice>
)

data class ChatChoice(
    val message: ChatMessage
)
