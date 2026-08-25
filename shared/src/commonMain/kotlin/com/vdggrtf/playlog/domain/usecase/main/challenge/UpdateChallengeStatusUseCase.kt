package com.vdggrtf.playlog.domain.usecase.main.challenge

import com.vdggrtf.playlog.domain.model.GameStatus
import com.vdggrtf.playlog.domain.repository.ChallengeRepository

class UpdateChallengeStatusUseCase (
    private val challengeRepository: ChallengeRepository
) {

    suspend operator fun invoke(challengeId: Int, newStatus: GameStatus): Result<Unit> {
        return challengeRepository.updateChallengeStatus(challengeId, newStatus)
    }
}