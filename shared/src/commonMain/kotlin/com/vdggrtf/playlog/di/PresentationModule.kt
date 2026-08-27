package com.vdggrtf.playlog.di

import com.vdggrtf.playlog.presentation.auth.login.LoginViewModel
import com.vdggrtf.playlog.presentation.auth.registrartion.RegistrationViewModel
import com.vdggrtf.playlog.presentation.main.game_details.GameDetailsViewModel
import com.vdggrtf.playlog.presentation.main.my_library.MyLibraryViewModel
import com.vdggrtf.playlog.presentation.main.my_library.scaner.ScannerViewModel
import com.vdggrtf.playlog.presentation.main.my_library.scaner.VerificationViewModel
import com.vdggrtf.playlog.presentation.main.profile.ProfileViewModel
import com.vdggrtf.playlog.presentation.main.recommendation.RecommendationViewModel
import com.vdggrtf.playlog.presentation.main.recommendation.ai.AiRecommendationGameViewModel
import com.vdggrtf.playlog.presentation.main.recommendation.custom_challenges.ChallengeBoardViewModel
import com.vdggrtf.playlog.presentation.main.recommendation.playlists.PlaylistDetailsViewModel
import com.vdggrtf.playlog.presentation.main.recommendation.search.SearchViewModel
import com.vdggrtf.playlog.presentation.main.recommendation.see_all.SeeAllGamesViewModel
import com.vdggrtf.playlog.presentation.splash.SplashScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        LoginViewModel(
            loginUseCase = get()
        )
    }

    viewModel {
        RegistrationViewModel(
            registrationUseCase = get()
        )
    }

    viewModel {
        GameDetailsViewModel(
            getLocalGameUseCase = get(),
            observeLocalGameStatusUseCase = get(),
            retryAiEvaluationUseCase = get(),
            finishGameWithRatingUseCase = get(),
            changeGameStatusUseCase = get(),
            fetchAndSyncRemoteGameUseCase = get(),
            getChallengesForGameUseCase = get(),
            savedStateHandle = get(),
            getBestGameDealUseCase = get(),
            getCommunityRatingUseCase = get(),
            observeMyPlaylistsUseCase = get(),
            addGameToPlaylistUseCase = get()
        )
    }

    viewModel {
        ScannerViewModel(
            scanAndImportLibraryUseCase = get()
        )
    }

    viewModel {
        VerificationViewModel(
            verifyGameCompletionUseCase = get()
        )
    }

    viewModel {
        MyLibraryViewModel(
            savedStateHandle = get(),
            observeMyLibraryUseCase = get(),
            getCompletedBountiesCountUseCase = get(),
            getTrackedBountyGameIdsUseCase = get(),
            createPlaylistUseCase = get(),
        )
    }

    viewModel {
        ProfileViewModel(
            observeCachedUserUseCase = get(),
            syncUserProfileUseCase = get(),
            observeProfileStatsUseCase = get(),
            getTotalBountyXpUseCase = get(),
            getCompletedBountiesCountUseCase = get(),
            observeMyPlaylistsUseCase = get(),
            syncPlaylistsUseCase = get(),
            logoutUseCase = get(),
        )
    }

    viewModel {
        AiRecommendationGameViewModel(
            getAiRecommendationsWithDetailsUseCase = get()
        )
    }

    viewModel {
        ChallengeBoardViewModel(
            savedStateHandle = get(),
            getCustomChallengesUseCase = get(),
            updateChallengeStatusUseCase = get(),
            verifyChallengeProofUseCase = get()
        )
    }

    viewModel {
        PlaylistDetailsViewModel(
            savedStateHandle = get(),
            observePlaylistGamesUseCase = get(),
            removeGameFromPlaylistUseCase = get()
        )
    }

    viewModel {
        SearchViewModel(
            searchGamesUseCase = get()
        )
    }

    viewModel {
        SeeAllGamesViewModel(
            savedStateHandle = get(),
            getPopularGamesUseCase = get(),
            getIndieGamesUseCase = get()
        )
    }

    viewModel {
        RecommendationViewModel(
            getPopularGamesUseCase = get(),
            getIndieGamesUseCase = get(),
            observeMyPlaylistsUseCase = get()
        )
    }

    viewModel {
        SplashScreenViewModel(
            checkUserSessionUseCase = get()
        )
    }
}