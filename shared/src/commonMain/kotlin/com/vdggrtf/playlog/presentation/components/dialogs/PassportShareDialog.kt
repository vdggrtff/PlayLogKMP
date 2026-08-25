package com.vdggrtf.playlog.presentation.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vdggrtf.playlog.presentation.components.profile.GamerPassportUi
import com.vdggrtf.playlog.ui.theme.CardBackground
import com.vdggrtf.playlog.utils.ShareManager
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import playlog.shared.generated.resources.Res.string
import playlog.shared.generated.resources.close
import playlog.shared.generated.resources.your_id_card

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun PassportShareDialog(
    nickname: String,
    totalGames: Int,
    completedGames: Int,
    favDifficulty: String,
    customChallengesCount: Int,
    totalBounty: Int,
    onDismiss: () -> Unit,
) {
    val graphicsLayer = rememberGraphicsLayer()
    val shareManager: ShareManager = koinInject()
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackground, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(string.your_id_card),
                color = Color.White,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box( modifier = Modifier
                .drawWithContent {
                    // Записываем всё, что внутри Box, в слой
                    graphicsLayer.record {
                        this@drawWithContent.drawContent()
                    }
                    // Отрисовываем на экране как обычно
                    drawLayer(graphicsLayer)
                }){
                GamerPassportUi(
                    nickname = nickname,
                    totalGames = totalGames,
                    completedGames = completedGames,
                    favDifficulty = favDifficulty,
                    customChallengesCount = customChallengesCount,
                    totalBounty = totalBounty
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Screenshot
            Button(
                onClick = {
                    // 💥 TRIGGER THE CAPTURE ASYNC INSIDE A COROUTINE!
                    scope.launch {
                        try {
                            // Capture the UI, wait for the result, and convert it to Android Bitmap
                            val imageBitmap = graphicsLayer.toImageBitmap()

                            // Share the bitmap using your existing ShareUtils
                            shareManager.shareImage(imageBitmap, "My Gamer Passport")
                        } catch (error: Throwable) {
                           println("PassportShare Failed to capture screenshot: ${error.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SHARE")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onDismiss) {
                Text(stringResource(string.close), color = Color.Gray)
            }
        }
    }
}