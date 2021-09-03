package com.benjaminwan.chinesettstflite.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.benjaminwan.chinesettstflite.common.fastspeechPath
import com.benjaminwan.chinesettstflite.common.melganPath
import com.benjaminwan.chinesettstflite.common.tacotronPath
import com.benjaminwan.chinesettstflite.common.zhLanguage
import com.benjaminwan.chinesettstflite.utils.FileUtils.isFileExists

class CheckVoiceData : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val result = TextToSpeech.Engine.CHECK_VOICE_DATA_PASS
        val returnData = Intent()

        val available: ArrayList<String> = arrayListOf()
        val unavailable: ArrayList<String> = arrayListOf()

        val isFastSpeedExist = isFileExists(fastspeechPath)
        val isTacotronExist = isFileExists(tacotronPath)
        val isMelganExist = isFileExists(melganPath)

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