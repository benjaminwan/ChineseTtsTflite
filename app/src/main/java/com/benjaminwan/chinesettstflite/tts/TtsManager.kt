package com.benjaminwan.chinesettstflite.tts

import android.content.Context
import android.media.AudioFormat
import android.speech.tts.SynthesisCallback
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.benjaminwan.chinesettstflite.app.App
import com.benjaminwan.chinesettstflite.models.SpeechPosInfo
import com.benjaminwan.chinesettstflite.models.SpeechPosInfo.Companion.emptyAudioData
import com.benjaminwan.chinesettstflite.models.TtsState
import com.benjaminwan.chinesettstflite.models.TtsType
import com.benjaminwan.chinesettstflite.utils.ZhProcessor
import com.benjaminwan.chinesettstflite.utils.copyAssetFileToDir
import com.benjaminwan.moshi.utils.moshiAny
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

object TtsManager {
    private const val SP_APP = "sp_tts_app"
    private const val SP_TTS_TYPE = "sp_tts_type"
    private const val SP_TTS_SPEED = "sp_tts_speed"

    private const val TTS_SAMPLE_RATE = 24000
    private const val TTS_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

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
    var ttsReady: Boolean
        get() = ttsReadyState.value
        set(value) {
            ttsReadyState.value = value
        }

    val ttsState: MutableState<TtsState> = mutableStateOf(TtsState())

    val ttsSpeedState: MutableState<Float> = mutableStateOf(1.0f)
    var ttsSpeed: Float
        get() = ttsSpeedState.value
        set(value) {
            ttsSpeedState.value = value
            spTtsSpeed = value
        }

    val ttsTypeState: MutableState<TtsType> = mutableStateOf(TtsType.FASTSPEECH2)
    var ttsType: TtsType
        get() = ttsTypeState.value
        set(value) {
            ttsTypeState.value = value
            spTtsType = value
        }

    val speechPosState: MutableState<SpeechPosInfo> = mutableStateOf(emptyAudioData)
    var speechPos: SpeechPosInfo
        get() = speechPosState.value
        set(value) {
            speechPosState.value = value
        }

    private var onSpeechDataListenerListener: OnSpeechDataListener? = null

    fun setOnSpeechDataListener(listener: OnSpeechDataListener?) {
        onSpeechDataListenerListener = listener
    }

    private var spTtsType: TtsType by App.INSTANCE
        .getSharedPreferences(SP_APP, Context.MODE_PRIVATE)
        .moshiAny(SP_TTS_TYPE, TtsType.FASTSPEECH2)

    private var spTtsSpeed: Float by App.INSTANCE
        .getSharedPreferences(SP_APP, Context.MODE_PRIVATE)
        .moshiAny(SP_TTS_SPEED, 1.0f)

    init {
        ttsType = spTtsType
        ttsSpeed = spTtsSpeed
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

    fun stop() {
        inputTextJob?.cancel()
        ttsState.value = TtsState(isStart = false)
    }

    fun speech(inputText: String, callback: SynthesisCallback? = null) {
        inputTextJob = flow {
            emit(inputText)
        }.flowOn(Dispatchers.IO)
            .catch { error -> error.printStackTrace() }
            .onStart {
                ttsState.value = TtsState(isStart = true)
                callback?.start(TTS_SAMPLE_RATE, TTS_AUDIO_FORMAT, 1)
                Logger.i("onStart")
            }
            .onEach { input ->
                val regex = Regex("[\n，。？?！!,;；]")
                val sentences = input.split(regex).filter { it.isNotBlank() }
                val size = sentences.size
                sentences.forEachIndexed { index, s ->
                    if (currentCoroutineContext().isActive) {
                        sentenceToSpeech(s, size, index + 1, callback)
                    }
                }
            }
            .onCompletion {
                ttsState.value = TtsState(isStart = false)
                speechPos = emptyAudioData
                callback?.done()
                Logger.i("onCompletion")
            }
            .launchIn(scope)
    }

    private suspend fun sentenceToSpeech(sentence: String, max: Int, current: Int, callback: SynthesisCallback?) {
        Logger.i("sentence=$sentence")
        val startTime = System.currentTimeMillis()
        val inputIds: IntArray = zhProcessor.text2ids(sentence)
        val tensorOutput: TensorBuffer? = when (ttsType) {
            TtsType.FASTSPEECH2 -> fastSpeech?.getMelSpectrogram(inputIds, ttsSpeed)
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
                speechPos = SpeechPosInfo(sentence, max, current)
                if (callback != null) {
                    writeToCallBack(callback, audioArray)
                }
                onSpeechDataListenerListener?.invoke(audioArray)
            }
            Logger.i("Vocoder Time cost=${vocoderTime - encoderTime}")
        }
    }

    private suspend fun writeToCallBack(callback: SynthesisCallback, audioFloat: FloatArray) {
        val audio = audioFloat.toByteArray()
        Logger.i("writeToCallBack:Float(${audioFloat.size}) Byte(${audio.size})")
        try {
            val maxBufferSize: Int = callback.maxBufferSize
            var offset = 0
            while (offset < audio.size && currentCoroutineContext().isActive) {
                val bytesToWrite = Math.min(maxBufferSize, audio.size - offset)
                callback.audioAvailable(audio, offset, bytesToWrite)
                offset += bytesToWrite
            }
        } catch (e: Exception) {
            Logger.e("Exception: $e")
        }
    }

    private fun FloatArray.toByteArray(): ByteArray {
        val shortArray = ShortArray(this.size)
        this.forEachIndexed { index, f ->
            shortArray[index] = (f * 32768F).toInt().toShort()
        }
        val buffer = ByteBuffer.allocate(4 * this.size)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        buffer.asShortBuffer().put(shortArray)
        return buffer.array()
    }

}