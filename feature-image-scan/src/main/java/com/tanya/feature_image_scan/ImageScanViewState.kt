package com.tanya.feature_image_scan

import androidx.compose.runtime.Immutable
import com.tanya.core_model.entity.TextScanResult

@Immutable
data class ImageScanViewState(
    val scanResult: TextScanResult = TextScanResult.Default,
    val loading: Boolean = false
) {
    companion object {
        val Empty = ImageScanViewState()
    }
}