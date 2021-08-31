package com.benjaminwan.chinesettstflite.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings

fun Context.gotoTtsSetting() {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    intent.action = "com.android.settings.TTS_SETTINGS"
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    this.startActivity(intent)
}