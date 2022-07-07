package com.tanya.core_domain.usescases

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.tanya.core_domain.SubjectUseCase
import com.tanya.core_domain.SuspendingWorkUseCase
import com.tanya.core_domain.usescases.ScanTextFromImageUseCase.Params
import com.tanya.core_text_recognition.CoreTextRecognition
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScanTextFromImageUseCase @Inject constructor(
    private val coreTextRecognition: CoreTextRecognition,
    @ApplicationContext val context: Context
): SuspendingWorkUseCase<Params, String>() {

    override suspend fun doWork(params: Params) = withContext(Dispatchers.Default) {
        coreTextRecognition.scanTextFromImage(params.inputImage)
    }

    data class Params(
        private val uri: Uri,
        private val context: Context
        )
    {
        val inputImage = InputImage.fromFilePath(context, uri)
    }

}