package com.tanya.feature_image_scan

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.tanya.core_resources.R
import com.tanya.core_ui.components.BottomSheetLayout
import com.tanya.core_ui.components.LightNavBars
import com.tanya.core_ui.components.PreviewContainer
import com.tanya.core_ui.theme.light_gray
import com.tanya.core_ui.util.rememberFlowWithLifeCycle
import kotlinx.coroutines.launch

@Composable
fun ImageScan() {
    LightNavBars()
    MainImageScan(viewModel = hiltViewModel())
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun MainImageScan(
    viewModel: ImageScanViewModel
) {
    val bottomSheetState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val state = rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = ImageScanViewState.Empty).value
    val scanResult = viewModel.scanResult.collectAsState(initial = "").value

    BottomSheetScaffold(
        sheetContent = {
            TextScanResult(
                state = state,
                scanResult = scanResult
            )
        },
        scaffoldState = bottomSheetState,
        sheetBackgroundColor = Color.White,
        sheetElevation = 4.dp,
        sheetPeekHeight = 56.dp,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        backgroundColor = Color.Black
    ) {
        ImageScan(
            viewModel = viewModel
        )
    }

    LaunchedEffect(null) {
        if (!state.loading &&  state.scanResult.isEmpty()) {
            scope.launch {
                bottomSheetState.bottomSheetState.expand()
            }
        }
    }
}

@Composable
fun TextScanResult(
    modifier: Modifier = Modifier,
    state: ImageScanViewState,
    scanResult: String
) {
    val title = when {
        state.loading -> stringResource(id = R.string.scanning_text)
        else -> {
            when {
                scanResult.isNotEmpty() -> stringResource(id = R.string.scan_results)
                else -> stringResource(id = R.string.text_not_found)
            }
        }
    }
    val text = when {
        state.scanResult.isNotEmpty() -> scanResult
        else -> stringResource(id = R.string.text_not_found_message)
    }
    val height = when {
        scanResult.isNotEmpty() -> 110.dp
        else -> 150.dp
    }
    val painter = when {
        scanResult.isNotEmpty() -> R.drawable.scan_result
        else -> R.drawable.no_data_illustration
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.handle),
                contentDescription = null,
                tint = light_gray,
                modifier = Modifier
                    .size(28.dp)
                    .padding(bottom = 4.dp)
                    .align(Alignment.TopCenter)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.align(Alignment.Center)
            )
            if (state.loading) {
                LinearProgressIndicator(
                    modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (!state.loading) {
            BottomSheetLayout(
                drawableId = painter,
                text = text,
                imageHeight = height
            ) {
                Button(
                    onClick = {},
                    elevation = null,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.button_save),
                        style = MaterialTheme.typography.caption,
                        fontSize = 13.sp,
                        modifier = Modifier.padding()
                    )
                }
            }
        }
    }
}

@Composable
internal fun ImageScan(
    viewModel: ImageScanViewModel
) {
    val imageUri = viewModel.decodeImageUri()
    Box {
        PreviewContainer(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
        ) {
            ImagePreview(
                imageUri = imageUri,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}

@Composable
fun ImagePreview(
    imageUri: Uri?,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(imageUri ?: "")
            .crossfade(1000)
            .size(Size.ORIGINAL)
            .build()
    )

    BoxWithConstraints(
        modifier = modifier,
    ) {
        val boxScope = this
        val ratio = boxScope.maxWidth/boxScope.maxHeight
        Surface(
            modifier = Modifier.matchParentSize(),
            color = Color.Black
        ) {
            Image(
                painter = painter,
                contentScale = ContentScale.FillHeight,
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .aspectRatio(ratio)
            )
        }
    }
}