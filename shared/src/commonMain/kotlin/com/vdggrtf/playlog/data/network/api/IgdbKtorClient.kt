package com.vdggrtf.playlog.data.network.api

import com.vdggrtf.playlog.data.network.dto.igdb.IgdbGameDto
import com.vdggrtf.playlog.data.network.response.igdb.TwitchTokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class IgdbKtorClient(
    private val httpClient: HttpClient,
    private val clientId: String,
    private val clientSecret: String
) {
    private var accessToken: String? = null

    // 1. УМНАЯ АВТОРИЗАЦИЯ (Сами берем токен, если его нет)
    private suspend fun ensureToken() {
        if (accessToken == null) {
            val response: TwitchTokenResponse = httpClient.post("https://id.twitch.tv/oauth2/token") {
                url {
                    parameters.append("client_id", clientId)
                    parameters.append("client_secret", clientSecret)
                    parameters.append("grant_type", "client_credentials")
                }
            }.body()
            accessToken = response.accessToken
            println("IGDB Auth: Получен новый токен!")
        }
    }

    // 2. ЗАПРОС ИГР НА ЯЗЫКЕ APICALYPSE
    suspend fun getGames(apicalypseQuery: String): List<IgdbGameDto> {
        ensureToken() // Проверяем токен перед каждым запросом!

        return httpClient.post("https://api.igdb.com/v4/games") {
            header("Client-ID", clientId)
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Text.Plain) // Ktor сам понимает, что это текст!
            setBody(apicalypseQuery) // Просто передаем строку!
        }.body() // 💥 Ktor сам десериализует JSON в List<IgdbGameDto>!
    }
}