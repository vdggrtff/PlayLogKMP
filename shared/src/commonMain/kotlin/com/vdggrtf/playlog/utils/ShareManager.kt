package com.vdggrtf.playlog.utils

import androidx.compose.ui.graphics.ImageBitmap

interface ShareManager {
    fun shareImage(imageBytes: ImageBitmap, title: String = "Gamer Passport")
}