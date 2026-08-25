package com.vdggrtf.playlog.di

import com.vdggrtf.playlog.BuildConfig
import com.vdggrtf.playlog.data.network.api.CheapSharkApiKtorClient
import com.vdggrtf.playlog.data.network.api.GeminiKtorClient
import com.vdggrtf.playlog.data.network.api.IgdbKtorClient
import com.vdggrtf.playlog.data.network.api.RawgKtorClient
import com.vdggrtf.playlog.data.network.api.RetroAchievementsKtorClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {

    // 1. Создаем единственный (Kind.Singleton) HttpClient для всего приложения
    single<HttpClient> {
        HttpClient {
            // Учим Ktor понимать JSON
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }
        }
    }

    single {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Auth)
            install(Postgrest)
        }
    }

    single<IgdbKtorClient> {
        IgdbKtorClient(
            httpClient = get(),
            clientId = BuildConfig.IGDB_CLIENT_ID,
            clientSecret = BuildConfig.IGDB_CLIENT_SECRET
        )
    }

    single {
        GeminiKtorClient(
            httpClient = get(),
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }

    single {
        RawgKtorClient(
            httpClient = get(),
            apiKey = BuildConfig.RAWG_API_KEY
        )
    }

    single {
        CheapSharkApiKtorClient(httpClient = get())
    }

    single {
        RetroAchievementsKtorClient(
            httpClient = get(),
            userName = BuildConfig.RA_USER,
            apiKey = BuildConfig.RA_API_KEY
        )
    }
}