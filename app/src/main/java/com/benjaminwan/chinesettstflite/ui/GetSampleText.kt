package com.benjaminwan.chinesettstflite.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.benjaminwan.chinesettstflite.R
import com.orhanobut.logger.Logger
import java.util.*

class GetSampleText : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locale = getLocaleFromIntent(intent)
        val language = locale.isO3Language
        var result = TextToSpeech.LANG_AVAILABLE
        val returnData = Intent()
        if (language == "zho") {
            returnData.putExtra(TextToSpeech.Engine.EXTRA_SAMPLE_TEXT, getString(R.string.zho_sample))
            Logger.i("Returned SampleText: " + getString(R.string.zho_sample))
        } else {
            Logger.i("Unsupported Language: $language")
            result = TextToSpeech.LANG_NOT_SUPPORTED
            returnData.putExtra("sampleText", "")
        }
        setResult(result, returnData)
        finish()
    }

    companion object {
        private fun getLocaleFromIntent(intent: Intent?): Locale {
            if (intent != null) {
                val language = intent.getStringExtra("language")
                if (language != null) {
                    return Locale(language)
                }
            }
            return Locale.getDefault()
        }
    }
}