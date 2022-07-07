package com.tanya.feature_image_capture

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.tanya.core_model.entity.TextScanResult
import com.tanya.core_resources.R
import com.tanya.core_ui.components.DarkSystemBars
import com.tanya.core_ui.components.PreviewContainer
import com.tanya.core_ui.extensions.executor
import com.tanya.core_ui.extensions.getCameraProvider
import com.tanya.core_ui.extensions.takePicture
import com.tanya.core_ui.permissions.CameraPermission
import com.tanya.core_ui.util.rememberFlowWithLifeCycle
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ImageCapture(
    openScanHistory: () -> Unit,
    onImageCapture: (File) -> Unit
) {
    DarkSystemBars()
    ImageCapture(
        openScanHistory = openScanHistory,
        modifier = Modifier.fillMaxSize()
    ) {
        onImageCapture(it)
    }
}

@Composable
internal fun ImageCapture(
    modifier: Modifier = Modifier,
    viewModel: ImageCaptureViewModel = hiltViewModel(),
    openScanHistory: () -> Unit,
    onImageCapture: (File) -> Unit
) {
    val state = rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = ImageCaptureViewState.Empty).value
    CameraPermission {
        ImageCaptureContent(
            openScanHistory = openScanHistory,
            modifier = modifier,
            scans = state.scans
        ) {
            onImageCapture(it)
        }
    }
}

@Composable
internal fun ImageCaptureContent(
    modifier: Modifier = Modifier,
    openScanHistory: () -> Unit,
    scans: List<TextScanResult>,
    context: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onImageCapture: (File) -> Unit
) {

    val imageUri = if (scans.isEmpty()) null else scans[0].imageUri.toUri()

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
                openScanHistory = openScanHistory,
                imageUri = imageUri,
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
    imageUri: Uri?,
    openScanHistory: () -> Unit,
    onImageCapture: () -> Unit,
    onUseCase: (UseCase) -> Unit
) {
    val context = LocalContext.current
    val painter = if (imageUri != null) {
        rememberAsyncImagePainter(
            model = ImageRequest
                .Builder(context)
                .data(imageUri)
                .size(Size.ORIGINAL)
                .build()
        )
    } else {
        painterResource(id = R.drawable.no_data_illustration)
    }

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
                color = Color.White,
                onClick = { openScanHistory() }
            ) {
                Box {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
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