package com.benjaminwan.chinesettstflite.ui

import androidx.lifecycle.ViewModel
import com.benjaminwan.chinesettstflite.tts.AudioPlayer
import com.benjaminwan.chinesettstflite.tts.TtsManager
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {

    private val audioPlayer by lazy {
        AudioPlayer()
    }

    init {
        TtsManager.setOnSpeechDataListener { audio ->
            runBlocking {
                audioPlayer.play(audio)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.terminate()
    }
}