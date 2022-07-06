package com.tanya.feature_image_scan

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageScanViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val imageUri: String? = savedStateHandle["imageUri"]

    init {
        Log.d("ImageScanViewModel", "$imageUri")
    }


}