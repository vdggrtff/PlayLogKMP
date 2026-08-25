package com.vdggrtf.playlog.domain.usecase.main.profile

import com.vdggrtf.playlog.domain.repository.LibraryRepository

class GetTotalBountyXpUseCase (
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getTotalBounty()
    }
}