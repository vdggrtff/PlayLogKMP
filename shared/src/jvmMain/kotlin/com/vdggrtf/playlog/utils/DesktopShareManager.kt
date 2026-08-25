package com.vdggrtf.playlog.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import java.io.File
import javax.imageio.ImageIO

class DesktopShareManager : ShareManager {
    override fun shareImage(imageBytes: ImageBitmap, title: String) {
        try {
            val awtImage = imageBytes.toAwtImage()
            val userHome = System.getProperty("user.home")
            val file = File(userHome, "GamerPassport_${System.currentTimeMillis()}.png")

            ImageIO.write(awtImage, "PNG", file)
            println("💥 Паспорт сохранен на ПК: ${file.absolutePath}")
        } catch (e: Exception) {
            println("Desktop Share Error: ${e.message}")
        }
    }
}