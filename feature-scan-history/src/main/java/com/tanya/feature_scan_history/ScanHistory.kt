package com.tanya.feature_scan_history

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.tanya.core_model.entity.TextScanResult
import com.tanya.core_model.utils.dateAsString
import com.tanya.core_resources.R
import com.tanya.core_ui.components.AppBar
import com.tanya.core_ui.components.ExpandingCard
import com.tanya.core_ui.components.LightSystemBars
import com.tanya.core_ui.util.rememberFlowWithLifeCycle

@Composable
fun ScanHistory(
    navigateUp: () -> Unit
) {
    LightSystemBars()
    Surface(
        color = Color.White,
        modifier = Modifier.systemBarsPadding()
    ) {
        ScanHistory(
            viewModel = hiltViewModel(),
            navigateUp = navigateUp
        )
    }
}

@Composable
internal fun ScanHistory(
    viewModel: ScanHistoryViewModel,
    navigateUp: () -> Unit
) {
    val state = rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = ScanHistoryViewState.Empty).value
    Scans(
        scans = state.scans,
        navigateUp = navigateUp
    )
}

@Composable
internal fun Scans(
    scans: List<TextScanResult>,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val listState = rememberLazyListState()
        Column(
           modifier = modifier.fillMaxSize()
        ) {
            AppBar(
                title = stringResource(id = R.string.scan_history),
                onClose = navigateUp
            )
            LazyColumn(
                state = listState,
            ) {
                items(scans) {
                    ScanItem(
                        scan = it,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ScanItem(
    scan: TextScanResult,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        onClick = { expanded = !expanded }
    ) {
        Row(
            modifier = modifier.padding(vertical = 16.dp)
        ) {
            ScanImage(
                path = scan.imageUri.toUri(),
                modifier = Modifier.size(80.dp)
            )
            ExpandingCard(
                title = scan.text,
                description = scan.text,
                icon = R.drawable.calendar,
                expanded = expanded,
                date = dateAsString(scan.dateCreated),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
        }
    }
}

@Composable
internal fun ScanImage(
    path: Uri,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Surface(
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(modifier = modifier) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest
                    .Builder(context)
                    .data(path)
                    .size(Size.ORIGINAL)
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}