package com.vdggrtf.playlog.domain.usecase.main.profile

import com.vdggrtf.playlog.domain.repository.AuthRepository

class SyncUserProfileUseCase (
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.syncUserProfile()
}