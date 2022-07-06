package com.tanya.core_text_recognition
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.lang.StringBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoreTextRecognition @Inject constructor() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun scanTextFromImage(image: InputImage): String {
        val detectedText = mutableSetOf<String>()
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                if (visionText.text.isNotBlank()) {
                    visionText.textBlocks.forEach { block ->
                        block.lines.forEach { line ->
                            line.elements.forEach { element ->
                                detectedText.add("${element.text} ")
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                // do nothing
            }

        val completedText = StringBuilder().also {
            detectedText.forEach { text -> it.append(text) }
        }

        return completedText.toString()
    }

}