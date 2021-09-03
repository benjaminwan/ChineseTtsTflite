package com.benjaminwan.chinesettstflite.common

import com.benjaminwan.chinesettstflite.app.App

const val FASTSPEECH2_NAME = "fastspeech2_quan.tflite"
const val TACOTRON2_NAME = "tacotron2_quan.tflite"
const val MELGAN_NAME = "mb_melgan.tflite"

const val zhLanguage = "zho-CHN"
val supportedLanguages = arrayListOf(zhLanguage)

val rootDir = App.INSTANCE.filesDir.absolutePath
const val FASTSPEED_VOICE = "zho-CHN-fastspeech"
const val TACOTRON_VOICE = "zho-CHN-tacotron"