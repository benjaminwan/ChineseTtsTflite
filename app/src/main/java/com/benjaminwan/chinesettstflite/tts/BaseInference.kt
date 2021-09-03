package com.benjaminwan.chinesettstflite.tts

import com.orhanobut.logger.Logger
import org.tensorflow.lite.Interpreter
import java.io.File

abstract class BaseInference(val file: File) {
    abstract val interpreter: Interpreter
    val options get() = Interpreter.Options().apply { setNumThreads(4) }

    fun printTensorInfo() {
        val inputCount = interpreter.inputTensorCount
        val inputTensorInfo = StringBuilder().appendLine(file.name)
        (0 until inputCount).forEach {
            val tensor = interpreter.getInputTensor(it)
            inputTensorInfo.appendLine(
                "inputTensor[$it]: name=${tensor.name()} shape=${
                    tensor.shape().contentToString()
                } dataType=${tensor.dataType()}"
            )
        }
        //Logger.i(inputTensorInfo.toString())
        val outputCount = interpreter.outputTensorCount
        val outputTensorInfo = StringBuilder().appendLine(file.name)
        (0 until outputCount).forEach {
            val tensor = interpreter.getOutputTensor(it)
            outputTensorInfo.appendLine(
                "outputTensor[$it]: name=${tensor.name()} shape=${
                    tensor.shape().contentToString()
                } dataType=${tensor.dataType()}"
            )
        }
        //Logger.i(outputTensorInfo.toString())
    }
}