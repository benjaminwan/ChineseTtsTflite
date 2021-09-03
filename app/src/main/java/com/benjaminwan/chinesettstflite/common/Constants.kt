package com.benjaminwan.chinesettstflite.common

import com.benjaminwan.chinesettstflite.app.App
import java.io.File

val targetDir = App.INSTANCE.filesDir.absolutePath
const val FASTSPEECH2_NAME = "fastspeech2_quan.tflite"
const val TACOTRON2_NAME = "tacotron2_quan.tflite"
const val MELGAN_NAME = "mb_melgan.tflite"
val fastspeechPath = targetDir + File.separator + FASTSPEECH2_NAME
val tacotronPath = targetDir + File.separator + TACOTRON2_NAME
val melganPath = targetDir + File.separator + MELGAN_NAME

const val zhLanguage = "zho-CHN"
val supportedLanguages = arrayListOf(zhLanguage)

const val FASTSPEED_VOICE = "zho-CHN-fastspeech"
const val TACOTRON_VOICE = "zho-CHN-tacotron"