package com.tanya.peek.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tanya.core_ui.theme.PeekTheme
import com.tanya.peek.navigation.PeekNavigation

@Composable
fun PeekApp() {
    PeekTheme {
        Surface(
            modifier = Modifier.fillMaxSize().navigationBarsPadding(),
            color = Color.Black
        ) {
            PeekNavigation()
        }
    }
}