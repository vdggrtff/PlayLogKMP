package com.vdggrtf.playlog.di

import com.vdggrtf.playlog.data.repositoryimpl.AiRepositoryImpl
import com.vdggrtf.playlog.data.repositoryimpl.AuthRepositoryImpl
import com.vdggrtf.playlog.data.repositoryimpl.ChallengeRepositoryImpl
import com.vdggrtf.playlog.data.repositoryimpl.GameRepositoryImpl
import com.vdggrtf.playlog.data.repositoryimpl.LibraryRepositoryImpl
import com.vdggrtf.playlog.data.repositoryimpl.PlaylistRepositoryImpl
import com.vdggrtf.playlog.data.repositoryimpl.RawgAchievementRepositoryImpl
import com.vdggrtf.playlog.data.repositoryimpl.RetroAchievementsRepositoryImpl
import com.vdggrtf.playlog.domain.repository.AiRepository
import com.vdggrtf.playlog.domain.repository.AuthRepository
import com.vdggrtf.playlog.domain.repository.ChallengeRepository
import com.vdggrtf.playlog.domain.repository.GameRepository
import com.vdggrtf.playlog.domain.repository.LibraryRepository
import com.vdggrtf.playlog.domain.repository.PlaylistRepository
import com.vdggrtf.playlog.domain.repository.RawgAchievementRepository
import com.vdggrtf.playlog.domain.repository.RetroAchievementsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<AiRepository> {
        AiRepositoryImpl(
            supabase = get(),
            geminiClient = get()
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            supabase = get(),
            userStorage = get(),
            dao = get()
        )
    }

    single<ChallengeRepository> {
        ChallengeRepositoryImpl(
            supabase = get(),
        )
    }

    // 💥 Аналог @Binds из Hilt. Говорим: "Если кто-то просит интерфейс GameRepository, отдай ему реализацию GameRepositoryImpl"
    single<GameRepository> {
        GameRepositoryImpl(
            api = get(), // Koin сам подставит сюда IgdbKtorClient!
            supabase = get(), // Supabase добавим чуть позже
            cheapSharkApi = get(),
        )
    }

    single<LibraryRepository> {
        LibraryRepositoryImpl(
            dao =  get(),
            supabase = get(),
        )
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            supabase = get(),
            playlistDao = get()
        )
    }

    single<RetroAchievementsRepository> {
        RetroAchievementsRepositoryImpl(
            api = get()
        )
    }

    single<RawgAchievementRepository> {
        RawgAchievementRepositoryImpl(
            api = get()
        )
    }
}