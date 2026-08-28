package com.vdggrtf.playlog.presentation.components.card_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vdggrtf.playlog.domain.model.AchievementDifficulty
import com.vdggrtf.playlog.domain.model.GameStatus
import com.vdggrtf.playlog.presentation.components.card.getDrawableRes
import com.vdggrtf.playlog.presentation.main.game_details.GameDetailsState
import com.vdggrtf.playlog.ui.theme.AiAccent
import com.vdggrtf.playlog.ui.theme.Background
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import playlog.shared.generated.resources.Res
import playlog.shared.generated.resources.completed_stamp
import playlog.shared.generated.resources.hide
import playlog.shared.generated.resources.n_a

@Composable
fun GameHeaderSection(state: GameDetailsState) {
    val game = state.game ?: return
    val images = state.screenshots.ifEmpty { listOf(game.imageUrl) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
    ) {
        HorizontalPager(
            state = rememberPagerState(pageCount = { images.size }),
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Background.copy(alpha = 0.9f),
                            Background
                        ),
                        startY = 400f
                    )
                )
        )

        // Content on top of pictures
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = game.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 28.sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(AiAccent.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                        .border(1.dp, AiAccent, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "⭐ ${game.rating ?: stringResource(Res.string.n_a)}",
                        color = AiAccent,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    game.releasedDate ?: stringResource(Res.string.hide),
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }

        // Demon print (if completed)
        if (state.currentGameStatus == GameStatus.COMPLETED && game.verifiedDifficulty != AchievementDifficulty.NONE) {
            Image(
                painter = painterResource(game.verifiedDifficulty.getDrawableRes()),
                contentDescription = stringResource(Res.string.completed_stamp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp)
                    .size(70.dp)
                    .rotate(-15f)
                    .alpha(0.8f)
            )
        }
    }
}