package com.vdggrtf.playlog.data.network.api

import com.vdggrtf.playlog.data.network.dto.gemini.GeminiContent
import com.vdggrtf.playlog.data.network.dto.gemini.GeminiGenerationConfig
import com.vdggrtf.playlog.data.network.dto.gemini.GeminiInlineData
import com.vdggrtf.playlog.data.network.dto.gemini.GeminiPart
import com.vdggrtf.playlog.data.network.dto.gemini.GeminiRequest
import com.vdggrtf.playlog.data.network.response.gemini.GeminiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.io.encoding.Base64

class GeminiKtorClient(
    private val httpClient: HttpClient,
    private val apiKey: String, // Ключ Gemini API из local.properties
) {

    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta/models"

    suspend fun generateText(
        prompt: String,
        modelName: String = "gemini-3.1-flash-lite-preview",
    ): String {
        val requestBody = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
            generationConfig = GeminiGenerationConfig(temperature = 0.1f)
        )

        val response: GeminiResponse =
            httpClient.post("$baseUrl/$modelName:generateContent?key=$apiKey") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }.body()

        return response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "ERROR"
    }

    suspend fun analyzeImage(
        prompt: String,
        imageBytes: ByteArray,
        modelName: String = "gemini-3.1-flash-lite-preview",
    ): String {

        val base64Image = Base64.encode(imageBytes)

        val requestBody = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(
                        GeminiPart(text = prompt),
                        GeminiPart(inlineData = GeminiInlineData(
                            mimeType = "image/jpeg",
                            data = base64Image
                        )
                        )
                    )
                )
            ),
            generationConfig = GeminiGenerationConfig(temperature = 0.1f)
        )

        val response: GeminiResponse = httpClient.post("$baseUrl/$modelName:generateContent?key=$apiKey") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }.body()

        return response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "ERROR"
    }
}