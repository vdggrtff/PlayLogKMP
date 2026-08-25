package com.vdggrtf.playlog.presentation.components.card

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vdggrtf.playlog.domain.model.AchievementDifficulty
import com.vdggrtf.playlog.presentation.components.AnimatedDemonWebpIcon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import playlog.shared.generated.resources.Res
import playlog.shared.generated.resources.achievement_counter_games
import playlog.shared.generated.resources.custom_challenge_face
import playlog.shared.generated.resources.custom_challenge_vortex
import playlog.shared.generated.resources.demon_difficult
import playlog.shared.generated.resources.easy_difficult
import playlog.shared.generated.resources.hard_difficult
import playlog.shared.generated.resources.impossible_difficult
import playlog.shared.generated.resources.medium_difficult

fun AchievementDifficulty.getDrawableRes(): DrawableResource = when (this) {
    AchievementDifficulty.EASY -> Res.drawable.easy_difficult          // Твои названия файлов в composeResources/drawable!
    AchievementDifficulty.MEDIUM -> Res.drawable.medium_difficult
    AchievementDifficulty.HARD -> Res.drawable.hard_difficult
    AchievementDifficulty.DEMON -> Res.drawable.demon_difficult
    AchievementDifficulty.IMPOSSIBLE -> Res.drawable.impossible_difficult
    AchievementDifficulty.CUSTOM_CHALLENGE -> Res.drawable.custom_challenge_vortex
    AchievementDifficulty.NONE -> Res.drawable.easy_difficult
}

@Composable
fun DifficultySquareCard(
    difficulty: AchievementDifficulty,
    count: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isUnlocked = count > 0

    val bgColor = if (isUnlocked) Color(0xFF1E1E26) else Color(0xFF15151C)
    val borderColor = if (isUnlocked) Color(0xFF6200EA) else Color.DarkGray
    val countColor = if (isUnlocked) Color(0xFF00E5FF) else Color.Gray

    Column(
        modifier = modifier
            .aspectRatio(0.85f)
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(if (isUnlocked) 2.dp else 1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .alpha(if (isUnlocked) 1f else 0.4f), // Эффект неактивности
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (difficulty == AchievementDifficulty.CUSTOM_CHALLENGE) {
            AnimatedImpossibleIcon(
                modifier = Modifier.size(60.dp)
            )
        } else if (difficulty == AchievementDifficulty.DEMON) {
            AnimatedDemonWebpIcon(
                modifier = Modifier.size(60.dp)
            )
        } else {
            Image(
                // 💥 Вызываем наш маппер и убираем "id ="!
                painter = painterResource(difficulty.getDrawableRes()),
                contentDescription = difficulty.title,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = difficulty.title,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(Res.string.achievement_counter_games, count),
            color = countColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun AnimatedImpossibleIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "impossible_anim")

    // 1. Вращение вихря (медленное и бесконечное)
    val vortexRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "vortex_rotation"
    )

    // 2. Дыхание лица (пульсация)
    val faceScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f, // Лицо увеличивается на 15%
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse // Туда-сюда
        ),
        label = "face_scale"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        // СЛОЙ 0: Черная Бездна (Закрывает белую дырку в вихре)
        Box(
            modifier = Modifier
                .fillMaxSize(0.6f) // Размер черной дыры
                .background(Color.Black, CircleShape)
        )

        // СЛОЙ 1: Крутящийся вихрь
        Image(
            painter = painterResource(Res.drawable.custom_challenge_vortex), // ТВОЕ НАЗВАНИЕ ФАЙЛА
            contentDescription = "Vortex",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = vortexRotation
                }
        )

        // СЛОЙ 2: Дышащее лицо демона
        Image(
            painter = painterResource(Res.drawable.custom_challenge_face), // ТВОЕ НАЗВАНИЕ ФАЙЛА
            contentDescription = "Face",
            modifier = Modifier
                .fillMaxSize(0.7f) // Чуть меньше вихря, чтобы влезть в центр
                .graphicsLayer {
                    scaleX = faceScale
                    scaleY = faceScale
                }
        )
    }
}