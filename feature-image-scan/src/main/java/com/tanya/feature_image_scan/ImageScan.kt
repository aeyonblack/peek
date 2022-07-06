package com.tanya.feature_image_scan

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.tanya.core_resources.R
import com.tanya.core_ui.components.LightNavBars
import com.tanya.core_ui.components.PreviewContainer
import com.tanya.core_ui.theme.light_gray

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
    BottomSheetScaffold(
        sheetContent = {
            TextScanResult()
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
fun TextScanResult(
    modifier: Modifier = Modifier
) {
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
                text = stringResource(id = R.string.scan_results),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.align(Alignment.Center)
            )
            LinearProgressIndicator(
                modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .align(Alignment.BottomCenter)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            color = MaterialTheme.colors.secondary.copy(alpha = 0f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.placeholder_text),
                style = MaterialTheme.typography.body1,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /*TODO*/ },
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            /*Icon(
                painter = painterResource(id = R.drawable.save),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp)
            )*/
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.button_save),
                style = MaterialTheme.typography.caption,
                fontSize = 13.sp,
                modifier = Modifier.padding()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
internal fun ImageScan(
    viewModel: ImageScanViewModel
) {
    val path = viewModel.imageUri
    val imageUri = path?.replace("(", "/")?.toUri()
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