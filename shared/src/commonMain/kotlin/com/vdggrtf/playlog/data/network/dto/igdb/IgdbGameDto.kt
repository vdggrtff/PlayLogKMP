package com.vdggrtf.playlog.data.network.dto.igdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IgdbGameDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("summary") val summary: String?, // Описание
    @SerialName("rating") val rating: Double?,   // Рейтинг (0 - 100)
    @SerialName("first_release_date") val firstReleaseDate: Long?, // Дата в секундах (Unix)
    @SerialName("cover") val cover: IgdbImageDto?,
    @SerialName("genres") val genres: List<IgdbNameDto>? = null,
    @SerialName("platforms") val platforms: List<IgdbNameDto>? = null,
    @SerialName("screenshots") val screenshots: List<IgdbImageDto>? = null
)

// Универсальный DTO для картинок (Обложки и Скриншоты)
data class IgdbImageDto(
    @SerialName("image_id") val imageId: String?
)

// Универсальный DTO для Жанров и Платформ (у них структура одинаковая)
data class IgdbNameDto(
    @SerialName("name") val name: String
)