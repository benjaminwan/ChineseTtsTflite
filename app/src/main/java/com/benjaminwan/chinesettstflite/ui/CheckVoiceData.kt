package com.benjaminwan.chinesettstflite.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.benjaminwan.chinesettstflite.common.*
import com.benjaminwan.chinesettstflite.utils.FileUtils.isFileExists
import java.io.File

class CheckVoiceData : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val result = TextToSpeech.Engine.CHECK_VOICE_DATA_PASS
        val returnData = Intent()

        val available: ArrayList<String> = arrayListOf()
        val unavailable: ArrayList<String> = arrayListOf()

        val isFastSpeedExist = isFileExists(rootDir + File.separator + FASTSPEECH2_NAME)
        val isTacotronExist = isFileExists(rootDir + File.separator + TACOTRON2_NAME)
        val isMelganExist = isFileExists(rootDir + File.separator + MELGAN_NAME)

        if ((isFastSpeedExist && isMelganExist) || (isTacotronExist && isMelganExist)) {
            available.add(zhLanguage)
        } else {
            unavailable.add(zhLanguage)
        }
        returnData.putStringArrayListExtra(TextToSpeech.Engine.EXTRA_AVAILABLE_VOICES, available)
        returnData.putStringArrayListExtra(TextToSpeech.Engine.EXTRA_UNAVAILABLE_VOICES, unavailable)
        setResult(result, returnData)
        finish()
    }
}