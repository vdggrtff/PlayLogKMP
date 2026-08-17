package com.vdggrtf.playlog

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PlayLog",
    ) {
        App()
    }
}