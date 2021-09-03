package com.benjaminwan.chinesettstflite.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Window
import com.benjaminwan.chinesettstflite.common.*
import com.benjaminwan.chinesettstflite.utils.FileUtils.isFileExists
import com.orhanobut.logger.Logger
import java.io.File

class DownloadVoiceData : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
    }
}