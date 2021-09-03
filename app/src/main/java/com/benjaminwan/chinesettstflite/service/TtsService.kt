package com.benjaminwan.chinesettstflite.service

import android.speech.tts.SynthesisCallback
import android.speech.tts.SynthesisRequest
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeechService
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.benjaminwan.chinesettstflite.tts.TtsManager
import com.orhanobut.logger.Logger
import java.util.*

class TtsService : TextToSpeechService() {
    private val currentLanguage: MutableState<Array<String>> = mutableStateOf(arrayOf("zho", "CHN", "fastspeech"))

    override fun onIsLanguageAvailable(_lang: String?, _country: String?, _variant: String?): Int {
        val lang = _lang ?: ""
        val country = _country ?: ""
        val variant = _variant ?: ""
        return if (Locale.SIMPLIFIED_CHINESE.isO3Language == lang || Locale.US.isO3Language == lang) {
            if (Locale.SIMPLIFIED_CHINESE.isO3Country == country || Locale.US.isO3Country == country) TextToSpeech.LANG_COUNTRY_AVAILABLE else TextToSpeech.LANG_AVAILABLE
        } else TextToSpeech.LANG_NOT_SUPPORTED
    }

    override fun onGetLanguage(): Array<String> {
        Logger.i("onGetLanguage: ${currentLanguage.value}")
        return currentLanguage.value
    }

    override fun onLoadLanguage(_lang: String?, _country: String?, _variant: String?): Int {
        val lang = _lang ?: ""
        val country = _country ?: ""
        val variant = _variant ?: ""
        Logger.i("$lang, $country, $variant")
        val result = onIsLanguageAvailable(lang, country, variant)
        currentLanguage.value = arrayOf<String>(lang, country, variant)
        return result
    }

    override fun onStop() {
        TtsManager.stop()
    }

    override fun onSynthesizeText(request: SynthesisRequest?, callback: SynthesisCallback?) {
        if (request == null || callback == null) return
        val load = onLoadLanguage(request.language, request.country, request.variant)
        if (load == TextToSpeech.LANG_NOT_SUPPORTED) {
            callback.error()
            return
        }
        if (!TtsManager.ttsReady) {
            callback.error()
            return
        }
        val text = request.charSequenceText.toString()
        TtsManager.speech(text, callback)
    }

    override fun onGetFeaturesForLanguage(lang: String?, country: String?, variant: String?): MutableSet<String> {
        val hashSet = HashSet<String>()
        hashSet.add(lang ?: "")
        hashSet.add(country ?: "")
        hashSet.add(variant ?: "")
        return hashSet
    }

    override fun onLoadVoice(voiceName: String?): Int {
        return TextToSpeech.SUCCESS
    }
}