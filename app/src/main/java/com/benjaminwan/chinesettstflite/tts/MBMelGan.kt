package com.benjaminwan.chinesettstflite.tts

import com.orhanobut.logger.Logger
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.FloatBuffer

class MBMelGan(file: File) : BaseInference(file) {
    override val interpreter: Interpreter = Interpreter(file, options)

    init {
        printTensorInfo()
    }

    fun getAudio(input: TensorBuffer): FloatArray {
        interpreter.resizeInput(0, input.shape)
        interpreter.allocateTensors()
        val outputBuffer = FloatBuffer.allocate(350000)
        val startTime = System.currentTimeMillis()
        interpreter.run(input.buffer, outputBuffer)
        //Logger.i("MBMelGan time cost: " + (System.currentTimeMillis() - startTime))
        val audioArray = FloatArray(outputBuffer.position())
        outputBuffer.rewind()
        outputBuffer[audioArray]
        return audioArray
    }
}