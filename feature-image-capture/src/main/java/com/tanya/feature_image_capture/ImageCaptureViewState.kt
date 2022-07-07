package com.tanya.feature_image_capture

import androidx.compose.runtime.Immutable
import com.tanya.core_model.entity.TextScanResult

@Immutable
internal data class ImageCaptureViewState(
    val scans: List<TextScanResult> = emptyList(),
    val loading: Boolean = false
) {
    companion object {
        val Empty = ImageCaptureViewState()
    }
}