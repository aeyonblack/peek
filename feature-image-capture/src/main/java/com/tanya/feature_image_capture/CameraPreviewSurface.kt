package com.tanya.feature_image_capture

import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

var previewView: PreviewView? = null

@Composable
internal fun CameraPreviewSurface(
    modifier: Modifier = Modifier,
    flashEnabled: Boolean = false,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    onUseCase: (UseCase) -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    previewView?.controller = null

    val controller = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector
                .Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
        }
    }

    DisposableEffect(true) {
        controller.bindToLifecycle(lifecycleOwner)
        onDispose {  }
    }

    controller.enableTorch(flashEnabled)

    previewView = PreviewView(context).apply {
        this.scaleType = scaleType
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    previewView!!.controller = controller

    AndroidView(
        modifier = modifier,
        factory = { _ -> previewView!!
            onUseCase(
                Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(previewView!!.surfaceProvider) }
            )
            previewView!!
        }
    )
}