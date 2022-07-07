package com.tanya.feature_image_scan

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.tanya.core_domain.usescases.ScanTextFromImageUseCase
import com.tanya.core_domain.usescases.ScanTextFromImageUseCase.Params
import com.tanya.core_model.TextScanResult
import com.tanya.core_ui.util.ObservableLoadingCounter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageScanViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val scanTextFromImageUseCase: ScanTextFromImageUseCase,
): ViewModel() {

    private val imageUri: String? = savedStateHandle["imageUri"]

    private val loadingState = ObservableLoadingCounter()

    val scanResult: Flow<TextScanResult> = scanTextFromImageUseCase.flow

    val state: StateFlow<ImageScanViewState> = combine(
        scanTextFromImageUseCase.flow,
        loadingState.observable
    ) { scanResult, loading ->
        ImageScanViewState(
            scanResult = scanResult,
            loading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ImageScanViewState.Empty
    )

    init {
        viewModelScope.launch {
            val job = launch {
                loadingState.addLoader()
                scanTextFromImageUseCase(
                    Params(
                        uri = decodeImageUri()!!,
                        context = scanTextFromImageUseCase.context
                    )
                )
            }
            job.invokeOnCompletion { loadingState.removeLoader() }
            job.join()
        }
    }

    fun decodeImageUri() = imageUri?.replace("(", "/")?.toUri()

}