package com.tanya.feature_image_scan

import androidx.compose.runtime.Immutable

@Immutable
data class ImageScanViewState(
    val scanResult: String = "",
    val loading: Boolean = false
) {
    companion object {
        val Empty = ImageScanViewState()
    }
}