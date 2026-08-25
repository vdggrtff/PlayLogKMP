package com.vdggrtf.playlog.di

import com.vdggrtf.playlog.domain.usecase.auth.login.LoginUseCase
import com.vdggrtf.playlog.domain.usecase.auth.reg.RegistrationUseCase
import com.vdggrtf.playlog.domain.usecase.main.ai.GetAiRecommendationsWithDetailsUseCase
import com.vdggrtf.playlog.domain.usecase.main.ai.GetGameRecommendationAiUseCase
import com.vdggrtf.playlog.domain.usecase.main.ai.ScanAndImportLibraryUseCase
import com.vdggrtf.playlog.domain.usecase.main.ai.VerifyGameCompletionUseCase
import com.vdggrtf.playlog.domain.usecase.main.challenge.GetChallengesForGameUseCase
import com.vdggrtf.playlog.domain.usecase.main.challenge.GetCustomChallengesUseCase
import com.vdggrtf.playlog.domain.usecase.main.challenge.GetTrackedBountyGameIdsUseCase
import com.vdggrtf.playlog.domain.usecase.main.challenge.UpdateChallengeStatusUseCase
import com.vdggrtf.playlog.domain.usecase.main.challenge.VerifyChallengeProofUseCase
import com.vdggrtf.playlog.domain.usecase.main.game.ChangeGameStatusUseCase
import com.vdggrtf.playlog.domain.usecase.main.game.FinishGameWithRatingUseCase
import com.vdggrtf.playlog.domain.usecase.main.game.GetBestGameDealUseCase
import com.vdggrtf.playlog.domain.usecase.main.game.GetCommunityRatingUseCase
import com.vdggrtf.playlog.domain.usecase.main.game.GetLocalGameUseCase
import com.vdggrtf.playlog.domain.usecase.main.game.ObserveLocalGameStatusUseCase
import com.vdggrtf.playlog.domain.usecase.main.game.RetryAiEvaluationUseCase
import com.vdggrtf.playlog.domain.usecase.main.library.GetCompletedBountiesCountUseCase
import com.vdggrtf.playlog.domain.usecase.main.library.ObserveMyLibraryUseCase
import com.vdggrtf.playlog.domain.usecase.main.playlist.AddGameToPlaylistUseCase
import com.vdggrtf.playlog.domain.usecase.main.playlist.CreatePlaylistUseCase
import com.vdggrtf.playlog.domain.usecase.main.playlist.ObserveMyPlaylistsUseCase
import com.vdggrtf.playlog.domain.usecase.main.playlist.ObservePlaylistGamesUseCase
import com.vdggrtf.playlog.domain.usecase.main.playlist.RemoveGameFromPlaylistUseCase
import com.vdggrtf.playlog.domain.usecase.main.playlist.SyncPlaylistsUseCase
import com.vdggrtf.playlog.domain.usecase.main.profile.GetTotalBountyXpUseCase
import com.vdggrtf.playlog.domain.usecase.main.profile.LogoutUseCase
import com.vdggrtf.playlog.domain.usecase.main.profile.ObserveCachedUserUseCase
import com.vdggrtf.playlog.domain.usecase.main.profile.ObserveProfileStatsUseCase
import com.vdggrtf.playlog.domain.usecase.main.profile.SyncUserProfileUseCase
import com.vdggrtf.playlog.domain.usecase.main.recommendation.GetIndieGamesUseCase
import com.vdggrtf.playlog.domain.usecase.main.recommendation.GetPopularGamesUseCase
import com.vdggrtf.playlog.domain.usecase.main.search.SearchGamesUseCase
import com.vdggrtf.playlog.domain.usecase.splash.CheckUserSessionUseCase
import org.koin.dsl.module


// 💥 UseCase-ы мы создаем через factory (Каждый раз создается новый легкий экземпляр, чтобы не жрать память)
val domainModule = module {

    //auth
    factory { LoginUseCase(authRepository = get()) }

    factory { RegistrationUseCase(authRepository = get()) }

    //ai

    factory { GetAiRecommendationsWithDetailsUseCase(aiRepository = get(), gameRepository = get()) }

    factory { GetGameRecommendationAiUseCase(aiRepository = get()) }

    factory { ScanAndImportLibraryUseCase(aiRepository = get(), gamesRepository = get(), libraryRepository = get()) }

    factory { VerifyGameCompletionUseCase(libraryRepository = get(), aiRepository = get()) }

    // challenge

    factory { GetChallengesForGameUseCase(challengeRepository = get()) }

    factory { GetCustomChallengesUseCase(challengeRepository = get()) }

    factory { GetTrackedBountyGameIdsUseCase(challengeRepository = get()) }

    factory { UpdateChallengeStatusUseCase(challengeRepository = get()) }

    factory { VerifyChallengeProofUseCase(challengeRepository = get(), aiRepository = get()) }

    // game

    factory { ChangeGameStatusUseCase(libraryRepository = get()) }

    factory { ChangeGameStatusUseCase(libraryRepository = get()) }

    factory { FinishGameWithRatingUseCase(libraryRepository = get()) }

    factory { GetBestGameDealUseCase(repository = get()) }

    factory { GetCommunityRatingUseCase(libraryRepository = get()) }

    factory { GetLocalGameUseCase(libraryRepository = get()) }

    factory { ObserveLocalGameStatusUseCase(libraryRepository = get()) }

    factory { RetryAiEvaluationUseCase(aiRepository = get(), gameRepository = get()) }

    // library
    factory { GetCompletedBountiesCountUseCase(supabase = get()) }

    factory { ObserveMyLibraryUseCase(libraryRepository = get()) }

    // playList

    factory { AddGameToPlaylistUseCase(repository = get()) }

    factory { CreatePlaylistUseCase(repository = get()) }

    factory { ObserveMyPlaylistsUseCase(repository = get()) }

    factory { ObservePlaylistGamesUseCase(libraryRepository = get()) }

    factory { RemoveGameFromPlaylistUseCase(repository = get()) }

    factory { SyncPlaylistsUseCase(repository = get()) }

    // profile

    factory { GetTotalBountyXpUseCase(repository = get()) }

    factory { LogoutUseCase(repository = get(), libraryRepository = get()) }

    factory { ObserveCachedUserUseCase(userStorage = get()) }

    factory { ObserveProfileStatsUseCase(repository = get()) }

    factory { SyncUserProfileUseCase(repository = get()) }

    // recommendation
    factory { GetIndieGamesUseCase(repository = get()) }

    factory { GetPopularGamesUseCase(repository = get()) }

    //search
    factory { SearchGamesUseCase(repository = get()) }


    factory { CheckUserSessionUseCase(repository = get()) }
}