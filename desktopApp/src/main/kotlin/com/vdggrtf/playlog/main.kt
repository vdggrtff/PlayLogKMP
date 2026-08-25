package com.vdggrtf.playlog

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.vdggrtf.playlog.di.createDesktopModule
import com.vdggrtf.playlog.di.initKoin

fun main() = application {

    initKoin(
        platformModule = createDesktopModule()
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "PlayLog",
    ) {
        App()
    }
}