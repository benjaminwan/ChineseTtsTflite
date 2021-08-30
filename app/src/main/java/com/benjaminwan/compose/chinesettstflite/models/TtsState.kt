package com.benjaminwan.compose.chinesettstflite.models

data class TtsState(
    val isStart: Boolean = false,
    val currentText: String = ""
) {
    val isStop get() = !isStart
}
