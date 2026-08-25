package com.vdggrtf.playlog.data.network.response.retro_achievements

import com.vdggrtf.playlog.data.network.dto.retro_achievements.RaAchievementDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RaGameExtendedResponse(
    @SerialName("ID") val id: Int,
    @SerialName("Achievements") val achievements: Map<String, RaAchievementDto>?
)