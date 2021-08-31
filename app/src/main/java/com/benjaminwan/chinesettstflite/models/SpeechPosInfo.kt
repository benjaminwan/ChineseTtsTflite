package com.benjaminwan.chinesettstflite.models

data class SpeechPosInfo(
    val text: String,
    val max: Int,
    val pos: Int,//form 1 to max
) {
    companion object {
        val emptyAudioData = SpeechPosInfo("", 0, 0)
    }
}
