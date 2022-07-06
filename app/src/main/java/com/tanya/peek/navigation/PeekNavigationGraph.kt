package com.tanya.peek.navigation

import android.util.Log
import androidx.core.net.toUri
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.tanya.feature_image_capture.ImageCapture
import com.tanya.feature_image_scan.ImageScan

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
            val imageUri = it.toURI().toString().replace("/","(")
            navController.navigate(LeafScreen.ImageScan.createRoute(root, imageUri))
            Log.d("Image Capture", it.absolutePath)
        }
    }
}

// Todo - figure out how to pass the correct navigation destination
fun NavGraphBuilder.addImageScan(
    navController: NavController,
    root: Screen
) {
    composable(
        route = LeafScreen.ImageScan.createRoute(root),
        arguments = listOf(
            navArgument("imageUri") { type = NavType.StringType }
        )
    ) {
        ImageScan()
    }
}