package com.vdggrtf.playlog.data.network.api

import com.vdggrtf.playlog.data.network.dto.retro_achievements.RaGameDto
import com.vdggrtf.playlog.data.network.response.retro_achievements.RaGameExtendedResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RetroAchievementsKtorClient(
    private val httpClient: HttpClient, // Наш движок
    private val userName: String,       // Твой логин RA (вместо интерцептора)
    private val apiKey: String          // Твой ключ RA (вместо интерцептора)
) {

    suspend fun getGamesForConsole(consoleId: Int): List<RaGameDto>{

        // httpClient.get("URL") - это аналог @GET из Retrofit
        return httpClient.get("https://retroachievements.org/API/API_GetGameList.php"){
            // Внутри этих скобок мы настраиваем запрос (это аналог @Query)
            parameter("i", consoleId)

            // 💥 МАГИЯ: Мы прямо тут подклеиваем ключи авторизации!
            // Больше не нужен никакой RaAuthInterceptor!
            parameter("z", userName)
            parameter("y", apiKey)
        }.body() // 💥 .body() автоматически превращает JSON-ответ в List<RaGameDto>
    }

    suspend fun getGameAchievements(gameId: Int): RaGameExtendedResponse {

        return httpClient.get("https://retroachievements.org/API/API_GetGameExtended.php"){
            parameter("i", gameId)

            parameter("z", userName)
            parameter("y", apiKey)
        }.body()
    }
}