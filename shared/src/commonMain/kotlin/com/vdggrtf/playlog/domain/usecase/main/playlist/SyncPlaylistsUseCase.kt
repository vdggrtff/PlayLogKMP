package com.vdggrtf.playlog.domain.usecase.main.playlist

import com.vdggrtf.playlog.domain.repository.PlaylistRepository

class SyncPlaylistsUseCase (
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke() = repository.syncPlaylists()
}