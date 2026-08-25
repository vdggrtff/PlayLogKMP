package com.vdggrtf.playlog.domain.usecase.main.challenge

import com.vdggrtf.playlog.domain.repository.ChallengeRepository

class GetTrackedBountyGameIdsUseCase (
    private val challengeRepository:  ChallengeRepository
) {
    suspend operator fun invoke(): Set<Int> {
        return challengeRepository.getTrackedBountyGameIds().getOrDefault(emptySet())
    }
}