package com.tanya.feature_scan_history

import androidx.compose.runtime.Immutable
import com.tanya.core_model.entity.TextScanResult

@Immutable
data class ScanHistoryViewState(
    val scans: List<TextScanResult> = emptyList(),
    val loading: Boolean = false
) {
    companion object {
        val Empty = ScanHistoryViewState()
    }
}