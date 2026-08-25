package com.vdggrtf.playlog.domain.usecase.main.ai

import com.vdggrtf.playlog.domain.model.AiGameRecommendation
import com.vdggrtf.playlog.domain.repository.AiRepository

class GetGameRecommendationAiUseCase (
    private val aiRepository: AiRepository
) {
    suspend operator fun invoke(userRequest: String): List<AiGameRecommendation>{
        return aiRepository.getGameRecommendation(userRequest)
    }
}