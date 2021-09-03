package com.benjaminwan.chinesettstflite.ui

import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.benjaminwan.chinesettstflite.app.App
import com.orhanobut.logger.Logger
import java.util.*

class MainViewModel : ViewModel() {
    private val onInitListener = object : OnInitListener {
        override fun onInit(status: Int) {
            ttsReady = status != TextToSpeech.ERROR
        }
    }

    private val progressListener = object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
            Logger.i("onStart=$utteranceId")
            isSpeak = true
        }

        override fun onDone(utteranceId: String?) {
            Logger.i("onDone=$utteranceId")
            isSpeak = false
        }

        override fun onError(utteranceId: String?) {
            Logger.i("onError=$utteranceId")
            isSpeak = false
        }

        override fun onStop(utteranceId: String?, interrupted: Boolean) {
            Logger.i("onStop=$utteranceId $interrupted")
            isSpeak = !interrupted
            super.onStop(utteranceId, interrupted)
        }

    }

    val tts = TextToSpeech(App.INSTANCE, onInitListener, "com.benjaminwan.chinesettstflite")

    val ttsReadyState: MutableState<Boolean> = mutableStateOf(false)
    var ttsReady: Boolean
        get() = ttsReadyState.value
        set(value) {
            ttsReadyState.value = value
        }

    val speakState: MutableState<Boolean> = mutableStateOf(false)
    private var isSpeak: Boolean
        get() = speakState.value
        set(value) {
            speakState.value = value
        }

    init {
        tts.setOnUtteranceProgressListener(progressListener)
    }

    fun sayText(text: String) {
        val result = tts.setLanguage(Locale.CHINESE)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Logger.e("setLanguage error!")
        } else {
            val speechResult = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, Random().nextInt().toString())
            Logger.i("speechResult=$speechResult")
        }
    }

    fun stop() {
        tts.stop()
    }

    override fun onCleared() {
        super.onCleared()
        tts.shutdown()
    }
}