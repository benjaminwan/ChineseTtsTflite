package com.benjaminwan.chinesettstflite.ui

import android.app.Activity
import android.os.Bundle
import android.view.Window

class DownloadVoiceData : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
    }
}