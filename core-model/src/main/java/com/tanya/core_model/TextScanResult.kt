package com.tanya.core_model

data class TextScanResult(
    val text: String = "",
    val success: Boolean = false
) {
    companion object {
        val Default = TextScanResult()
    }
}