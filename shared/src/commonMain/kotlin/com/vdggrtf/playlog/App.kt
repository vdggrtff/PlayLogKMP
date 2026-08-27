package com.vdggrtf.playlog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vdggrtf.playlog.presentation.MainLayout
import com.vdggrtf.playlog.ui.theme.PlayLogTheme

@Composable
@Preview
fun App() {
    PlayLogTheme {
        // 💥 2. Вызываем твой главный экран с нижней панелью навигации!
        MainLayout() // (Или AppNavGraph(), если у тебя нет MainLayout)

    }
}