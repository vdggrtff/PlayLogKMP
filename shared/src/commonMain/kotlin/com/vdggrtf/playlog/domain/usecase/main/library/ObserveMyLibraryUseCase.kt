package com.vdggrtf.playlog.domain.usecase.main.library

import com.vdggrtf.playlog.domain.model.GameModel
import com.vdggrtf.playlog.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow

class ObserveMyLibraryUseCase (
    private val libraryRepository: LibraryRepository
) {
    operator fun invoke(): Flow<List<GameModel>> {
        return libraryRepository.getMyLibrary() // Всё! Гениально и просто.
    }
}