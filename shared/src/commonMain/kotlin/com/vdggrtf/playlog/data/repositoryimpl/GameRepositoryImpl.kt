package com.vdggrtf.playlog.data.repositoryimpl

import com.vdggrtf.playlog.data.mapper.toDomainModel
import com.vdggrtf.playlog.data.network.api.CheapSharkApiKtorClient
import com.vdggrtf.playlog.data.network.api.IgdbKtorClient
import com.vdggrtf.playlog.data.network.dto.cheapshark.CheapSharkDealDto
import com.vdggrtf.playlog.data.network.dto.supabase.challenges.CashedGameDto
import com.vdggrtf.playlog.domain.model.GameModel
import com.vdggrtf.playlog.domain.repository.GameRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class GameRepositoryImpl (
    private val supabase: SupabaseClient,
    private val cheapSharkApi: CheapSharkApiKtorClient,
    private val api: IgdbKtorClient,
) : GameRepository {

    private val baseFields = "fields id, name, summary, rating, first_release_date, cover.image_id, genres.name, platforms.name;"

    override suspend fun searchGames(
        query: String,
        page: Int,
        dates: String?,
        genres: String?,
        platforms: String?,
    ): Result<List<GameModel>> {
        return try {
            val offset = (page - 1) * 40

            var filterClause = "cover != null"
            if (!dates.isNullOrBlank()) filterClause += " & $dates"
            if (!genres.isNullOrBlank()) filterClause += " & genres = ($genres)"
            if (!platforms.isNullOrBlank()) filterClause += " & platforms = ($platforms)"


            // В IGDB поиск работает невероятно круто через слово search!
            val apicalypseQuery = "search \"$query\"; $baseFields where $filterClause; limit 40; offset $offset;"

            val response = api.getGames(apicalypseQuery)

            val games = response.map { it.toDomainModel() }
            Result.success(games)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getPopularGames(
        page: Int,
        dates: String?,
        genres: String?,
        platforms: String?,
    ): Result<List<GameModel>> {
        return try {
            val offset = (page - 1) * 40

            // Пишем запрос на языке Apicalypse!
            // Просим сортировать по количеству оценок (самые популярные) и брать только игры с обложками
            var filterClause = "cover != null & rating_count >= 10" // Только игры с обложкой и оценками
            if (!dates.isNullOrBlank()) filterClause += " & $dates"
            if (!genres.isNullOrBlank()) filterClause += " & genres = ($genres)"     // IGDB синтаксис: genres = (8,12)
            if (!platforms.isNullOrBlank()) filterClause += " & platforms = ($platforms)"

            val query = "$baseFields where $filterClause; sort rating_count desc; limit 40; offset $offset;"

            println("IGDB_DEBUG: Запрос -> $query")

            // 💥 2. МАГИЯ KTOR!
            // Больше никаких RequestBody. Просто передаем строку.
            // Он сам сходит за токеном, сам сделает запрос и сам спарсит JSON в список!
            val dtos = api.getGames(query)

            val games = dtos.map { it.toDomainModel() }

            Result.success(games)
        } catch (e: Exception){
            println("IGDB_ERROR: Ошибка загрузки -> ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getGameDetails(gameId: Int): Result<GameModel> {
        return try {
            val query = "$baseFields fields screenshots.image_id; where id = $gameId;"

            val dtos = api.getGames(query)
            val gameDto = dtos.firstOrNull()
            if (gameDto != null){
                Result.success(gameDto.toDomainModel())
            } else {
                Result.failure(Exception("Game not found"))
            }
        }catch (e: Exception){
            println("IGDB_ERROR: Ошибка загрузки деталей -> ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getCachedGame(id: Int): CashedGameDto? {
        return withContext(Dispatchers.IO) {
            try {
                supabase.from("global_games_cache")
                    .select { filter { eq("id", id) } }
                    .decodeList<CashedGameDto>()
                    .firstOrNull()
            } catch (e: Exception) {
                // if the token has expired - attempting to refresh and retry.
                if (e.message?.contains("JWT expired") == true) {
                    println("RAWG_CACHE ⚠️ Токен протух при чтении! Обновляем...")
                    try {
                        supabase.auth.refreshCurrentSession()
                        return@withContext supabase.from("global_games_cache")
                            .select { filter { eq("id", id) } }
                            .decodeList<CashedGameDto>()
                            .firstOrNull()
                    } catch (refreshEx: Exception) {
                        println("RAWG_CACHE ❌ Не удалось обновить токен: ${refreshEx.message}")
                    }
                }
                println("RAWG_CACHE Ошибка чтения кэша: ${e.message}")
                null
            }
        }
    }

    override suspend fun saveToCache(cacheDto: CashedGameDto) {
        withContext(Dispatchers.IO) {
            try {
                supabase.from("global_games_cache").upsert(cacheDto)
               println("RAWG_CACHE 💾 АБСОЛЮТНЫЙ КЭШ СОХРАНЕН!")
            } catch (e: Exception) {
                if (e.message?.contains("JWT expired") == true) {
                    println("RAWG_CACHE ⚠️ Токен протух при записи! Обновляем...")
                    try {
                        supabase.auth.refreshCurrentSession()
                        supabase.from("global_games_cache").upsert(cacheDto)
                       println("RAWG_CACHE 💾 АБСОЛЮТНЫЙ КЭШ СОХРАНЕН (со 2-й попытки)!")
                        return@withContext
                    } catch (refreshEx: Exception) {
                        // ignoring, if it didn't work, it didn't work.
                    }
                }
                println("RAWG_CACHE Ошибка записи кэша: ${e.message}")
            }
        }
    }

    override suspend fun getScreenshots(id: Int): Result<List<String>> {
        return try {
            val query = "fields screenshots.image_id; where id = $id;"
            val dtos = api.getGames(query)

            val gameDto = dtos.firstOrNull()

            val screenUrls = gameDto?.screenshots?.mapNotNull {
                it.imageId?.let { imgId -> "https://images.igdb.com/igdb/image/upload/t_1080p/$imgId.jpg" }
            } ?: emptyList()

            Result.success(screenUrls)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGamePrices(gameName: String): Result<List<CheapSharkDealDto>> {
        return try {
             Result.success(cheapSharkApi.getStoreSpecificDeals(gameName))
        } catch (e: Exception) {
            println("SHARK Акула не ответила: ${e.message}")
            Result.failure(e)
        }
    }
}