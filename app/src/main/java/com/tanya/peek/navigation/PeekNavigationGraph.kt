package com.tanya.peek.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.tanya.feature_image_capture.ImageCapture
import com.tanya.feature_image_scan.ImageScan
import com.tanya.feature_scan_history.ScanHistory

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
        addScanHistory(
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
        ImageCapture(
            openScanHistory = {
                navController.navigate(LeafScreen.ScanHistory.createRoute(root)) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        ) {
            // navigate to image scan and pass image as argument
            val imageUri = it.toURI().toString().replace("/","(")
            navController.navigate(LeafScreen.ImageScan.createRoute(root, imageUri)) {
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}

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
        ImageScan {
            navController.navigate(LeafScreen.ScanHistory.createRoute(root)) {
                popUpTo(LeafScreen.ImageScan.createRoute(root)) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}

fun NavGraphBuilder.addScanHistory(
    navController: NavController,
    root: Screen
) {
    composable(LeafScreen.ScanHistory.createRoute(root)) {
        ScanHistory(navigateUp = navController::navigateUp)
    }
}
