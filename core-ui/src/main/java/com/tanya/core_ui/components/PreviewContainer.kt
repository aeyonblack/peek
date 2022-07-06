package com.tanya.core_ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tanya.core_resources.R

@Composable
fun PreviewContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(0.92f)
        ) {
            content()
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