package com.vdggrtf.playlog.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
        title = { Text("iOS Upload", color = Color.White) },
        text = { Text("Camera and Gallery are not implemented for iOS yet.", color = Color.Gray) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}