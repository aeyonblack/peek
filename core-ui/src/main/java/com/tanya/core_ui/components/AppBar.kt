package com.tanya.core_ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tanya.core_resources.R

@Composable
fun AppBar(
    title: String,
    onClose: (() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = { onClose?.invoke() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}