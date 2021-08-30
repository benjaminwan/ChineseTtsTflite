package com.benjaminwan.compose.chinesettstflite.tts

import com.orhanobut.logger.Logger
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.FloatBuffer
import java.util.*

class Tacotron2(file: File) : BaseInference(file) {
    override val interpreter: Interpreter = Interpreter(file, options)

    init {
        printTensorInfo()
    }

    fun getMelSpectrogram(inputIds: IntArray): TensorBuffer {
        interpreter.resizeInput(0, intArrayOf(1, inputIds.size))
        interpreter.allocateTensors()

        val outputMap: MutableMap<Int, Any> = HashMap()
        val outputBuffer = FloatBuffer.allocate(350000)
        outputMap[0] = outputBuffer

        val inputs = Array(1) { IntArray(inputIds.size) }
        inputs[0] = inputIds
        val startTime = System.currentTimeMillis()
        interpreter.runForMultipleInputsOutputs(
            arrayOf<Any>(inputs, intArrayOf(inputIds.size), intArrayOf(0)),
            outputMap
        )
        Logger.i("time cost: ${System.currentTimeMillis() - startTime}")

        val size: Int = interpreter.getOutputTensor(0).shape()[2]
        val shape = intArrayOf(1, outputBuffer.position() / size, size)
        val spectrogram = TensorBuffer.createFixedSize(shape, DataType.FLOAT32)
        val outputArray = FloatArray(outputBuffer.position())
        outputBuffer.rewind()
        outputBuffer[outputArray]
        spectrogram.loadArray(outputArray)
        return spectrogram
    }
}