package com.benjaminwan.chinesettstflite.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech

class CheckVoiceData : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val result = TextToSpeech.Engine.CHECK_VOICE_DATA_PASS
        val returnData = Intent()
        returnData.putStringArrayListExtra(TextToSpeech.Engine.EXTRA_AVAILABLE_VOICES, supportedLanguages)
        setResult(result, returnData)
        finish()
    }

    companion object {
        val supportedLanguages = arrayListOf("zho-CHN", "eng-USA")
    }
}