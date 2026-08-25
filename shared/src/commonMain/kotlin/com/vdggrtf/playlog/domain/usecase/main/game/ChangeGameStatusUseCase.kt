package com.vdggrtf.playlog.domain.usecase.main.game

import com.vdggrtf.playlog.domain.model.AchievementDifficulty
import com.vdggrtf.playlog.domain.model.GameModel
import com.vdggrtf.playlog.domain.model.GameStatus
import com.vdggrtf.playlog.domain.repository.LibraryRepository

class ChangeGameStatusUseCase (
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(game: GameModel, newStatus: GameStatus, aiDifficulty: AchievementDifficulty) {
        if (newStatus == GameStatus.NONE){
            libraryRepository.deleteGameFromLibrary(game)
        } else {
            val updatedGame = game.copy(
                status = newStatus,
                aiDifficulty = aiDifficulty
            )
            libraryRepository.addGameToLibrary(updatedGame)
        }
    }

}