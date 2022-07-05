package com.tanya.feature_image_capture


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
import com.tanya.core_resources.R
import com.tanya.core_ui.extensions.getCameraProvider
import com.tanya.core_ui.permissions.CameraPermission
import java.io.File

@Composable
fun ImageCapture() {
    ImageCapture {
        // Todo - do something with the image
    }
}

@Composable
internal fun ImageCapture(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageCapture: (File) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    CameraPermission {
        Box {

            var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }

            val imageCaptureUseCase by remember {
                mutableStateOf(
                    ImageCapture
                        .Builder()
                        .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build()
                )
            }

            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.BottomCenter)
                ) {
                    Box(
                        modifier = Modifier.fillMaxHeight(0.92f)
                    ) {
                        CameraPreview(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(bottom = 2.dp)
                        ) {
                            previewUseCase = it
                        }
                        Image(
                            painter = painterResource(id = R.drawable.preview_foreground),
                            contentDescription = null,
                            modifier = Modifier.fillMaxHeight(),
                            alignment = Alignment.BottomCenter
                        )
                        Image(
                            painter = painterResource(id = R.drawable.peek_logo_transparent),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .size(128.dp)
                                .align(Alignment.TopCenter),
                        )
                        Box(
                            modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .padding(16.dp)
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
                                        color = Color.Black.copy(alpha = 0.5f)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_history),
                                            contentDescription = null,
                                            modifier = Modifier.padding(8.dp),
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
                                    modifier = Modifier.padding(12.dp),
                                    color = Color.White,
                                    shape = RoundedCornerShape(50),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.search_text),
                                        contentDescription = null,
                                        modifier = Modifier.padding(18.dp)
                                    )
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter,
                    ) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = stringResource(id = R.string.scan_text),
                                color = Color.Black,
                                style = MaterialTheme.typography.caption,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .align(Alignment.Top)
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                    }
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
}

@Composable
internal fun CameraPreview(
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