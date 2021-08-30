package com.benjaminwan.compose.chinesettstflite.tts

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.benjaminwan.compose.chinesettstflite.models.AudioData
import com.orhanobut.logger.Logger
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

object AudioPlayer {
    private const val SAMPLE_RATE = 24000
    private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_FLOAT
    private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO
    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()
    private val audioFormat = AudioFormat.Builder()
        .setSampleRate(SAMPLE_RATE)
        .setChannelMask(CHANNEL_CONFIG)
        .setEncoding(AUDIO_FORMAT)
        .build()
    private val minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
    private val audioTrack = AudioTrack(audioAttributes, audioFormat, minBufferSize, AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE)

    init {
        audioTrack.play()
    }

    fun stop() {
        audioTrack.stop()
    }

    suspend fun play(audioData: AudioData) {
        val audio = audioData.audio
        try {
            var index = 0
            while (index < audio.size && currentCoroutineContext().isActive) {
                val buffer = Math.min(minBufferSize, audio.size - index)
                audioTrack.write(audio, index, buffer, AudioTrack.WRITE_BLOCKING)
                index += minBufferSize
            }
        } catch (e: Exception) {
            Logger.e("Exception: $e")
        }
    }

}