package com.vdggrtf.playlog.navigation

import com.vdggrtf.playlog.navigation.Screen.Companion.ACHIEVE_HUNTING_SCREEN
import com.vdggrtf.playlog.navigation.Screen.Companion.LIBRARY_SCREEN
import com.vdggrtf.playlog.navigation.Screen.Companion.PROFILE_SCREEN
import com.vdggrtf.playlog.navigation.Screen.Companion.RECOMMENDATION_SCREEN
import org.jetbrains.compose.resources.DrawableResource
import playlog.shared.generated.resources.Res
import playlog.shared.generated.resources.achievement_icon
import playlog.shared.generated.resources.library_icon
import playlog.shared.generated.resources.profile_icon
import playlog.shared.generated.resources.search_icon

sealed class BottomBarItems(val route: String, val title: String, val icon: DrawableResource) {

    object Library :
        BottomBarItems(route = LIBRARY_SCREEN, title = "LIBRARY", icon = Res.drawable.library_icon)

    object Home : BottomBarItems(
        route = RECOMMENDATION_SCREEN,
        title = "DISCOVERY",
        icon = Res.drawable.search_icon
    )

    object Achievements : BottomBarItems(
        route = ACHIEVE_HUNTING_SCREEN,
        title = "TROPHIES",
        icon = Res.drawable.achievement_icon
    )

    object Profile :
        BottomBarItems(route = PROFILE_SCREEN, title = "PROFILE", icon = Res.drawable.profile_icon)
}