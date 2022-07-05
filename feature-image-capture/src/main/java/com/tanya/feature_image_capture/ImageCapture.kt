package com.tanya.feature_image_capture

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ImageCapture() {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Image Capture",
                style = MaterialTheme.typography.h2
            )
        }
    }
}