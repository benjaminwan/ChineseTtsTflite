package com.benjaminwan.chinesettstflite.service

import android.speech.tts.SynthesisCallback
import android.speech.tts.SynthesisRequest
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeechService
import com.benjaminwan.chinesettstflite.tts.TtsManager
import com.orhanobut.logger.Logger
import java.util.*

class TtsService : TextToSpeechService() {
    private val currentLanguage: MutableList<String> = mutableListOf("zho", "CHN", "")

    override fun onIsLanguageAvailable(_lang: String?, _country: String?, _variant: String?): Int {
        val lang = _lang ?: ""
        val country = _country ?: ""
        val variant = _variant ?: ""
        return if (Locale.SIMPLIFIED_CHINESE.isO3Language == lang || Locale.US.isO3Language == lang) {
            if (Locale.SIMPLIFIED_CHINESE.isO3Country == country || Locale.US.isO3Country == country) TextToSpeech.LANG_COUNTRY_AVAILABLE else TextToSpeech.LANG_AVAILABLE
        } else TextToSpeech.LANG_NOT_SUPPORTED
    }

    override fun onGetLanguage(): Array<String> {
        Logger.i("onGetLanguage: ${currentLanguage.toTypedArray()}")
        return currentLanguage.toTypedArray()
    }

    override fun onLoadLanguage(_lang: String?, _country: String?, _variant: String?): Int {
        val lang = _lang ?: ""
        val country = _country ?: ""
        val variant = _variant ?: ""
        Logger.i("$lang, $country, $variant")
        val result = onIsLanguageAvailable(lang, country, variant)
        currentLanguage.clear()
        currentLanguage.addAll(arrayOf<String>(lang, country, variant))
        return result
    }

    override fun onStop() {
        TtsManager.stop()
    }

    override fun onSynthesizeText(request: SynthesisRequest?, callback: SynthesisCallback?) {
        if (request == null || callback == null) return
        val language = request.language
        val country = request.country
        val variant = request.variant
        val text = request.charSequenceText.toString()

        val load = onIsLanguageAvailable(language, country, variant)
        if (load == TextToSpeech.LANG_NOT_SUPPORTED) {
            callback.error()
            return
        }
        if (!TtsManager.isReady) {
            callback.error()
            return
        }
        Logger.i("text=$text")
        TtsManager.speechAsync(text, callback)
    }
}