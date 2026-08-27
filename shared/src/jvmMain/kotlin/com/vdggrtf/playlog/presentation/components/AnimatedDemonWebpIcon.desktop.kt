package com.vdggrtf.playlog.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import playlog.shared.generated.resources.Res.drawable
import playlog.shared.generated.resources.demon_animate_mvp

@Composable
actual fun AnimatedDemonWebpIcon(modifier: Modifier) {
    // На ПК пока рисуем статичный кадр или через системный рендерер
    AsyncImage(
        model = drawable.demon_animate_mvp,
        contentDescription = "Demon",
        modifier = modifier
    )
}