package com.tanya.core_ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpandingCard(
    title: String,
    description: String,
    icon: Int,
    modifier: Modifier = Modifier,
    date: String,
    shape: Shape = RoundedCornerShape(0.dp),
    expanded: Boolean = false,
) {
    Surface(
        modifier = Modifier
            .animateContentSize(
                tween(
                    durationMillis = 750,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = shape,
    ) {
        Column(modifier) {
            Row {
                Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = date,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.body2,
                maxLines = if (!expanded) 3 else Integer.MAX_VALUE,
                fontSize = 13.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}