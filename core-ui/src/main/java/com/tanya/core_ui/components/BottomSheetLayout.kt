package com.tanya.core_ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomSheetLayout(
    modifier: Modifier = Modifier,
    @DrawableRes drawableId: Int,
    text: String,
    imageHeight: Dp,
    content: @Composable () -> Unit
) {
    val painter = painterResource(id = drawableId)
    val ratio = painter.intrinsicSize.width/painter.intrinsicSize.height
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .height(imageHeight)
                    .aspectRatio(ratio)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.caption,
            color = Color.Black.copy(alpha = 0.7f),
            fontSize = 13.sp,
            maxLines = 5,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}