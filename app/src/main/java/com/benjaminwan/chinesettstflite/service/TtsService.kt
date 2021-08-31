package com.benjaminwan.chinesettstflite.service

import android.speech.tts.SynthesisCallback
import android.speech.tts.SynthesisRequest
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeechService
import java.util.*

class TtsService : TextToSpeechService() {

    @Volatile
    private var mCurrentLanguage: Array<String> = emptyArray()

    override fun onIsLanguageAvailable(lang: String?, country: String?, variant: String?): Int {
        return if (Locale.SIMPLIFIED_CHINESE.isO3Language == lang || Locale.US.isO3Language == lang) {
            if (Locale.SIMPLIFIED_CHINESE.isO3Country == country || Locale.US.isO3Country == country) TextToSpeech.LANG_COUNTRY_AVAILABLE else TextToSpeech.LANG_AVAILABLE
        } else TextToSpeech.LANG_NOT_SUPPORTED
    }

    override fun onGetLanguage(): Array<String> {
        return mCurrentLanguage
    }

    override fun onLoadLanguage(lang: String, country: String, variant: String?): Int {
        mCurrentLanguage = arrayOf<String>(lang, country, "")
        return onIsLanguageAvailable(lang, country, variant)
    }

    override fun onStop() {

    }

    override fun onSynthesizeText(p0: SynthesisRequest?, p1: SynthesisCallback?) {

    }
}