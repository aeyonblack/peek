package com.tanya.feature_image_scan

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.tanya.core_model.entity.TextScanResult
import com.tanya.core_model.utils.getCurrentDateTime
import com.tanya.core_resources.R
import com.tanya.core_ui.components.BottomSheetLayout
import com.tanya.core_ui.components.LightNavBars
import com.tanya.core_ui.components.PreviewContainer
import com.tanya.core_ui.theme.light_gray
import com.tanya.core_ui.util.rememberFlowWithLifeCycle
import kotlinx.coroutines.launch

@Composable
fun ImageScan(
    openScanHistory: () -> Unit
) {
    LightNavBars()
    MainImageScan(viewModel = hiltViewModel()) {
        openScanHistory()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun MainImageScan(
    viewModel: ImageScanViewModel,
    openScanHistory: () -> Unit
) {
    val bottomSheetState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val state = rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = ImageScanViewState.Empty).value
    val scanResult = viewModel.scanResult.collectAsState(initial = TextScanResult.Default).value

    BottomSheetScaffold(
        sheetContent = {
            ScanResult(
                state = state,
                scanResult = scanResult,
                viewModel = viewModel,
                openScanHistory = openScanHistory,
                onCloseSheet = {
                    scope.launch {
                        bottomSheetState.bottomSheetState.collapse()
                    }
                }
            ) {
                scope.launch {
                    bottomSheetState.bottomSheetState.expand()
                }
            }
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

}

@Composable
fun ScanResult(
    modifier: Modifier = Modifier,
    viewModel: ImageScanViewModel,
    state: ImageScanViewState,
    scanResult: TextScanResult,
    openScanHistory: () -> Unit,
    onCloseSheet: () -> Unit,
    onScanComplete: () -> Unit,
) {
    val title = when {
        !scanResult.success -> stringResource(id = R.string.scanning_text)
        else -> {
            when {
                scanResult.text.isNotBlank() -> stringResource(id = R.string.scan_results)
                else -> stringResource(id = R.string.text_not_found)
            }
        }
    }
    var text = stringResource(id = R.string.text_not_found_message)
    var height = 150.dp
    var painter = R.drawable.no_data_illustration
    var bottomSheetAction: BottomSheetAction = BottomSheetAction.Close
    if (scanResult.text.isNotBlank()) {
        text = scanResult.text
        height = 110.dp
        painter = R.drawable.scan_result
        bottomSheetAction = BottomSheetAction.Keep
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
            if (!scanResult.success) {
                LinearProgressIndicator(
                    modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (scanResult.success) {
            BottomSheetLayout(
                drawableId = painter,
                text = text,
                imageHeight = height,
                buttonText = bottomSheetAction.actionText
            ) {
                when (bottomSheetAction) {
                    is BottomSheetAction.Keep -> {
                        viewModel.saveScan(
                            scanResult.copy(
                                dateCreated = getCurrentDateTime(),
                                imageUri = viewModel.decodeImageUri()!!.toString()
                            )
                        )
                        openScanHistory()
                    }
                    else -> onCloseSheet()
                }
            }
            onScanComplete()
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

sealed class BottomSheetAction(val actionText: String) {
    object Keep : BottomSheetAction("Keep")
    object Close: BottomSheetAction("Close")
}