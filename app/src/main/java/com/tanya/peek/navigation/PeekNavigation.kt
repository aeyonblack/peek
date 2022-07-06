package com.tanya.peek.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

/**
 * Top level screens
 */
sealed class Screen(val route: String) {
    object ImageCapture: Screen("image_capture")
    object ScanHistory: Screen("scan_history")
}

/**
 * Leaf screens that can only be accessed from the top level
 */
sealed class LeafScreen(private val route: String) {

    fun createRoute(root: Screen) = "${root.route}/$route"

    object ImageCapture: LeafScreen("image_capture")
    object ScanHistory: LeafScreen("scan_history")

    object ImageScan: LeafScreen("scan/{imageUri}") {
        fun createRoute(root: Screen, imageUri: String) = "${root.route}/scan/$imageUri"
    }

}

/**
 * Defines the nav graph and orchestrates navigation between screens
 */
@Composable
fun PeekNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ImageCapture.route,
        modifier = modifier
    ) {
        addImageCaptureTopLevel(
            navController= navController
        )
    }
}