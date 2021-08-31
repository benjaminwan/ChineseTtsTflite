package com.benjaminwan.chinesettstflite.models

data class AudioData(
    val text: String,
    val audio: FloatArray,
    val max: Int,
    val pos: Int,//form 1 to max
) {
    companion object {
        val emptyAudioData = AudioData("", FloatArray(0), 0, 0)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AudioData

        if (text != other.text) return false
        if (!audio.contentEquals(other.audio)) return false
        if (max != other.max) return false
        if (pos != other.pos) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + audio.contentHashCode()
        result = 31 * result + max
        result = 31 * result + pos
        return result
    }

}
