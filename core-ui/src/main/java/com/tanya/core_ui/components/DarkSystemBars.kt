package com.tanya.core_ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DarkSystemBars() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setNavigationBarColor(
            color = Color.Black,
            darkIcons = false
        )
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }
}

@Composable
fun LightSystemBars() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setNavigationBarColor(
            color = Color.White,
            darkIcons = true
        )
        systemUiController.setStatusBarColor(
            color = Color.White,
            darkIcons = true
        )
    }
}

@Composable
fun LightNavBars() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = Color.White,
            darkIcons = true
        )
    }
}