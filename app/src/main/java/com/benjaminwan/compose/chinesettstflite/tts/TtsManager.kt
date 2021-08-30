package com.benjaminwan.compose.chinesettstflite.tts

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.benjaminwan.compose.chinesettstflite.models.AudioData
import com.benjaminwan.compose.chinesettstflite.models.AudioData.Companion.emptyAudioData
import com.benjaminwan.compose.chinesettstflite.models.TtsState
import com.benjaminwan.compose.chinesettstflite.models.TtsType
import com.benjaminwan.compose.chinesettstflite.utils.ZhProcessor
import com.benjaminwan.compose.chinesettstflite.utils.copyAssetFileToDir
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

object TtsManager {
    private const val FASTSPEECH2 = "fastspeech2_quan.tflite"
    private const val TACOTRON2 = "tacotron2_quan.tflite"
    private const val MELGAN = "mb_melgan.tflite"

    private var fastSpeech: FastSpeech2? = null
    private var tacotron: Tacotron2? = null
    private var melGan: MBMelGan? = null

    private val scope = CoroutineScope(Dispatchers.IO)
    private var inputTextJob: Job? = null
    private lateinit var zhProcessor: ZhProcessor

    val ttsReadyState: MutableState<Boolean> = mutableStateOf(false)
    private var ttsReady: Boolean
        get() = ttsReadyState.value
        set(value) {
            ttsReadyState.value = value
        }

    val ttsState: MutableState<TtsState> = mutableStateOf(TtsState())
    val speedState: MutableState<Float> = mutableStateOf(1.0f)
    var speed: Float
        get() = speedState.value
        set(value) {
            speedState.value = value
        }
    val ttsTypeState: MutableState<TtsType> = mutableStateOf(TtsType.FASTSPEECH2)
    var ttsType: TtsType
        get() = ttsTypeState.value
        set(value) {
            ttsTypeState.value = value
        }

    val currentSpeechState: MutableState<AudioData> = mutableStateOf(emptyAudioData)
    var currentSpeech: AudioData
        get() = currentSpeechState.value
        set(value) {
            currentSpeechState.value = value
        }

    fun initModels(context: Context) {
        zhProcessor = ZhProcessor(context)
        val fastspeechFile = copyAssetFileToDir(context, FASTSPEECH2, context.filesDir.absolutePath)
        val tacotronFile = copyAssetFileToDir(context, TACOTRON2, context.filesDir.absolutePath)
        val vocoderFile = copyAssetFileToDir(context, MELGAN, context.filesDir.absolutePath)
        if (fastspeechFile == null || tacotronFile == null || vocoderFile == null) {
            Logger.e("TtsManager初始化失败:模型文件复制错误!")
            ttsReady = false
            return
        }
        fastSpeech = FastSpeech2(fastspeechFile)
        tacotron = Tacotron2(tacotronFile)
        melGan = MBMelGan(vocoderFile)
        ttsReady = true
    }

    private suspend fun sentenceToSpeech(sentence: String, max: Int, current: Int) {
        Logger.i("sentence=$sentence")
        val startTime = System.currentTimeMillis()
        val inputIds: IntArray = zhProcessor.text2ids(sentence)
        val tensorOutput: TensorBuffer? = when (ttsType) {
            TtsType.FASTSPEECH2 -> fastSpeech?.getMelSpectrogram(inputIds, speed)
            TtsType.TACOTRON2 -> tacotron?.getMelSpectrogram(inputIds)
        }
        val encoderTime = System.currentTimeMillis()
        Logger.i("Encoder Time cost=${encoderTime - startTime}")
        if (tensorOutput != null) {
            val audioArray: FloatArray? = try {
                melGan?.getAudio(tensorOutput)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            val vocoderTime = System.currentTimeMillis()
            if (audioArray != null) {
                val audioData = AudioData(sentence, audioArray, max, current)
                currentSpeech = audioData
                AudioPlayer.play(audioData)
            }
            Logger.i("Vocoder Time cost=${vocoderTime - encoderTime}")
        }
    }

    fun stop() {
        inputTextJob?.cancel()
        ttsState.value = TtsState(isStart = false)
    }

    fun input(inputText: String) {
        inputTextJob = flow {
            emit(inputText)
        }.flowOn(Dispatchers.IO)
            .catch { error -> error.printStackTrace() }
            .onStart { ttsState.value = TtsState(isStart = true) }
            .onEach { input ->
                val regex = Regex("[\n，。？?！!,;；]")
                val sentences = input.split(regex).filter { it.isNotBlank() }
                val size = sentences.size
                sentences.forEachIndexed { index, s ->
                    if (currentCoroutineContext().isActive) {
                        sentenceToSpeech(s, size, index + 1)
                    }
                }
            }
            .onCompletion {
                ttsState.value = TtsState(isStart = false)
                currentSpeech = emptyAudioData
            }
            .launchIn(scope)
    }
}