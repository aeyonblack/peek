package com.tanya.peek.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.tanya.feature_image_capture.ImageCapture

/**
 * Defines the navigation graph, a set of screens
 * and all the screens that can be reached from them and
 * those they can be reached from
 */

fun NavGraphBuilder.addImageCaptureTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.ImageCapture.route,
        startDestination = LeafScreen.ImageCapture.createRoute(Screen.ImageCapture)
    ) {
        addImageCapture(
            navController = navController,
            root = Screen.ImageCapture
        )
        addImageScan(
            navController = navController,
            root = Screen.ImageCapture
        )
    }
}

fun NavGraphBuilder.addImageCapture(
    navController: NavController,
    root: Screen
) {
    composable(LeafScreen.ImageCapture.createRoute(root)) {
        ImageCapture {
            // navigate to image scan and pass image as argument
        }
    }
}

fun NavGraphBuilder.addImageScan(
    navController: NavController,
    root: Screen
) {
    composable(LeafScreen.ImageScan.createRoute(root)) {

    }
}