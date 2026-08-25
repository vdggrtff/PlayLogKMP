package com.vdggrtf.playlog.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class AndroidShareManager(private val context: Context) : ShareManager {
    override fun shareImage(imageBytes: ImageBitmap, title: String) {
        try {
            val androidBitmap = imageBytes.asAndroidBitmap()

            // Сохраняем в PNG
            val imagesDir = File(context.cacheDir, "shared_images").apply { mkdirs() }
            val file = File(imagesDir, "passport_${System.currentTimeMillis()}.png")
            val outputStream = FileOutputStream(file)
            androidBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Получаем безопасный Uri
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            // Открываем нативную шторку "Поделиться" Андроида!
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val chooser = Intent.createChooser(intent, title).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            println("Share Error: ${e.message}")
        }
    }
}