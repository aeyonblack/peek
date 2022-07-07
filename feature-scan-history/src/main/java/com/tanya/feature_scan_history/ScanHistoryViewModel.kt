package com.tanya.feature_scan_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanya.core_domain.observers.ObserveAllScans
import com.tanya.core_ui.util.ObservableLoadingCounter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class ScanHistoryViewModel @Inject constructor(
    observeAllScans: ObserveAllScans,
): ViewModel() {

    private val loadingState = ObservableLoadingCounter()

    val state: StateFlow<ScanHistoryViewState> = combine(
        observeAllScans.flow,
        loadingState.observable
    ) { scans, loading ->
        ScanHistoryViewState(
            scans = scans,
            loading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScanHistoryViewState.Empty
    )

    init {
        observeAllScans(ObserveAllScans.Params())
    }

}