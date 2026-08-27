package com.vdggrtf.playlog.presentation.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import playlog.shared.generated.resources.Res
import playlog.shared.generated.resources.demon_animate_mvp

@Composable
actual fun AnimatedDemonWebpIcon(
    modifier: Modifier
) {
    val platformContext = LocalPlatformContext.current

    val imageLoader = remember(platformContext) {
        ImageLoader.Builder(platformContext)
            .components {
                if (SDK_INT >= 28) {
                    add(AnimatedImageDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    AsyncImage(
        model = Res.drawable.demon_animate_mvp,
        imageLoader = imageLoader,
        contentDescription = "Animated Demon",
        modifier = modifier
    )
}