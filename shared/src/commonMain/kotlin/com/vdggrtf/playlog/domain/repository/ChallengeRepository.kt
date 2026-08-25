package com.vdggrtf.playlog.domain.repository

interface ChallengeRepository {
    // Fetches all available global challenges from Supabase
    suspend fun getChallenges(): Result<List<com.vdggrtf.playlog.domain.model.CustomChallengeModel>>

    // Fetches only completed challenge IDs for the current user
    suspend fun getCompletedChallengeIds(): Result<List<Int>>

    // Updates or deletes the challenge status in Supabase
    suspend fun updateChallengeStatus(challengeId: Int, newStatus: com.vdggrtf.playlog.domain.model.GameStatus): Result<Unit>

    // Fetches a map of challenge IDs to their current game statuses for the active user
    suspend fun getUserChallengeStatuses(): Result<Map<Int, com.vdggrtf.playlog.domain.model.GameStatus>>

    suspend fun getChallengesByGameId(gameId: Int): Result<List<com.vdggrtf.playlog.domain.model.CustomChallengeModel>>

    suspend fun getTrackedBountyGameIds(): Result<Set<Int>>
}