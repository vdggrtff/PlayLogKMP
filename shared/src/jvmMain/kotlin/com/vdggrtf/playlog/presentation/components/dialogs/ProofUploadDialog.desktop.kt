package com.vdggrtf.playlog.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import java.awt.FileDialog
import java.awt.Frame
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

@Composable
actual fun ProofUploadDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onImageReady: (ByteArray) -> Unit
) {
    if (!showDialog) return

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E26),
        title = { Text("Submit Proof", color = Color.White, fontWeight = FontWeight.Bold) },
        text = { Text("Select an image from your computer to prove completion.", color = Color.Gray) },
        confirmButton = {
            Button(
                onClick = {
                    // 💥 Открываем стандартное окно выбора файла на ПК!
                    val dialog = FileDialog(null as Frame?, "Select Image", FileDialog.LOAD)
                    dialog.isVisible = true
                    val file = dialog.file?.let { File(dialog.directory, it) }

                    if (file != null) {
                        try {
                            val image = ImageIO.read(file)
                            val baos = ByteArrayOutputStream()
                            ImageIO.write(image, "jpg", baos) // Легкая компрессия
                            onImageReady(baos.toByteArray())
                        } catch (e: Exception) {
                            println("Desktop Image Error: ${e.message}")
                        }
                    }
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
            ) {
                Text("OPEN FILE PICKER", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCEL", color = Color.Gray) }
        }
    )
}