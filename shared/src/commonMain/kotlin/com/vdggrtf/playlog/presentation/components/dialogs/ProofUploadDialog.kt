package com.vdggrtf.playlog.presentation.components.dialogs

import androidx.compose.runtime.Composable

// 💥 Утилита для сжатия (чтобы код был чистым)
@Composable
expect fun ProofUploadDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onImageReady: (ByteArray) -> Unit
)
