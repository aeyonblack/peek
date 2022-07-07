package com.tanya.core_text_recognition
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.tanya.core_model.entity.TextScanResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class CoreTextRecognition @Inject constructor() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun scanTextFromImage(image: InputImage): TextScanResult {

        val detectedText = mutableSetOf<String>()

        val completedText = suspendCoroutine { continuation ->
            recognizer.process(image).continueWith {
                it.addOnSuccessListener { visionText ->
                    var completedText = StringBuilder()
                    if (visionText.text.isNotBlank()) {
                        visionText.textBlocks.forEach { block ->
                            block.lines.forEach { line ->
                                line.elements.forEach { element ->
                                    detectedText.add("${element.text} ")
                                    Log.d("CoreText", "element: ${element.text}")
                                }
                            }
                        }
                        completedText = StringBuilder().also {
                            detectedText.forEach { text -> it.append(text) }
                        }
                    }
                    continuation.resume(completedText.toString())
                }
            }
        }

        Log.d("CoreText", "completedText: $completedText")

        return TextScanResult(
            text = completedText,
            success = true
        )
    }
}