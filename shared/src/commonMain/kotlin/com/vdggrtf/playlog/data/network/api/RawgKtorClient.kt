package com.vdggrtf.playlog.data.network.api

import com.vdggrtf.playlog.data.network.response.rawg.RawgAchievementResponse
import com.vdggrtf.playlog.data.network.response.rawg.RawgSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RawgKtorClient(
    private val httpClient: HttpClient,
    private val apiKey: String // Ключ от RAWG (Передадим через Koin)
) {
    // 1. Ищем игру по имени, чтобы украсть её ID (берем только 1 результат для скорости)
    suspend fun searchGameForId(gameName: String): RawgSearchResponse{
        return httpClient.get("https://api.rawg.io/api/games") {
            parameter("key", apiKey)
            parameter("search", gameName)
            parameter("page_size", 1)
        }.body()
    }

    // 2. Зная ID, выкачиваем список ачивок
    suspend fun getAchievements(gameId: Int): RawgAchievementResponse {
        return httpClient.get("https://api.rawg.io/api/games/$gameId/achievements") {
            parameter("key", apiKey)
            parameter("page_size", 100) // Берем сразу с запасом
        }.body()
    }

}