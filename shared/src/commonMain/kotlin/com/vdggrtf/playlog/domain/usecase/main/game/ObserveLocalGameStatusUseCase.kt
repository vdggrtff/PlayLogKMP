package com.vdggrtf.playlog.domain.usecase.main.game

import com.vdggrtf.playlog.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.map

class ObserveLocalGameStatusUseCase (
    private val libraryRepository: LibraryRepository
) {
    operator fun invoke(gameId: Int) = libraryRepository.getMyLibrary().map { library ->
        library.find { it.id == gameId }
    }
}