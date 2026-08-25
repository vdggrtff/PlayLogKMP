package com.vdggrtf.playlog.presentation.components.dialogs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vdggrtf.playlog.utils.createTempPictureUri
import java.io.ByteArrayOutputStream

private fun Uri.toCompressedByteArray(context: Context): ByteArray? {
    return try {
        val bitmap = if (Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        }
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.toByteArray()
    } catch (e: Exception) {
        null
    }
}

// 💥 КЛЮЧЕВОЕ СЛОВО ACTUAL!
@Composable
actual fun ProofUploadDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onImageReady: (ByteArray) -> Unit
) {
    if (!showDialog) return

    val context = LocalContext.current
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && tempPhotoUri != null) {
            val bytes = tempPhotoUri!!.toCompressedByteArray(context)
            if (bytes != null) onImageReady(bytes)
        }
        onDismiss()
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bytes = it.toCompressedByteArray(context)
            if (bytes != null) onImageReady(bytes)
        }
        onDismiss()
    }

    // Твой UI диалога
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E26),
        title = { Text("Submit Proof", color = Color.White, fontWeight = FontWeight.Bold) },
        text = { Text("How do you want to upload your screenshot?", color = Color.Gray) },
        confirmButton = {
            Button(
                onClick = {
                    tempPhotoUri = context.createTempPictureUri()
                    cameraLauncher.launch(tempPhotoUri!!)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("CAMERA", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("GALLERY", color = Color.White)
            }
        }
    )
}