package com.tanya.feature_image_capture

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.tanya.core_resources.R
import com.tanya.core_ui.components.PreviewContainer
import com.tanya.core_ui.extensions.executor
import com.tanya.core_ui.extensions.getCameraProvider
import com.tanya.core_ui.extensions.takePicture
import com.tanya.core_ui.permissions.CameraPermission
import com.tanya.core_ui.theme.peek_yellow
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ImageCapture(
    onImageCapture: (File) -> Unit
) {
    ImageCapture(
        modifier = Modifier.fillMaxSize()
    ) {
        onImageCapture(it)
    }
}

@Composable
internal fun ImageCapture(
    modifier: Modifier = Modifier,
    onImageCapture: (File) -> Unit
) {
    CameraPermission {
        ImageCaptureContent {
            onImageCapture(it)
        }
    }
}

@Composable
internal fun ImageCaptureContent(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onImageCapture: (File) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        val coroutineScope = rememberCoroutineScope()
        var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
        val imageCaptureUseCase by remember {
            mutableStateOf(
                ImageCapture
                    .Builder()
                    .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
            )
        }

        PreviewContainer(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
        ) {
            CameraPreview(
                modifier = Modifier.matchParentSize(),
                onImageCapture = {
                    coroutineScope.launch {
                        onImageCapture(imageCaptureUseCase.takePicture(context.executor))
                    }
                }
            ) {
                previewUseCase = it
            }
        }

        LaunchedEffect(previewUseCase) {
            val cameraProvider = context.getCameraProvider()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    previewUseCase,
                    imageCaptureUseCase
                )
            } catch (e: Exception) {
                Log.e("Image Capture", "Failed to bind camera use cases", e)
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun CameraPreview(
    modifier: Modifier = Modifier,
    onImageCapture: () -> Unit,
    onUseCase: (UseCase) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        CameraPreviewSurface(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 2.dp)
        ) {
            onUseCase(it)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(24.dp)
                    .size(48.dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    ),
                color = Color.White
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.test_img),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        modifier = Modifier.matchParentSize(),
                        color = Color.Black.copy(alpha = 0.4f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_history),
                            contentDescription = null,
                            modifier = Modifier.padding(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }
            Surface(
                modifier = Modifier
                    .size(96.dp)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(50),
                color = Color.White.copy(alpha = 0.35f),
                border = BorderStroke(2.dp,Color.White)
            ) {
                Surface(
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    shape = RoundedCornerShape(50),
                    onClick = onImageCapture
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.document),
                        contentDescription = null,
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }
        }
    }
}


@Composable
internal fun CameraPreviewSurface(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    onUseCase: (UseCase) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            onUseCase(
                Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }
            )
            previewView
        }
    )
}