package com.example.apdhelper.ml

import android.content.Context
import android.content.res.AssetFileDescriptor
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
    val fileDescriptor: AssetFileDescriptor = context.assets.openFd(modelName)
    val inputStream = fileDescriptor.createInputStream()
    val fileChannel = inputStream.channel
    return fileChannel.map(
        FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength
    )
}

fun runModel(context: Context, inputData: FloatArray): Float {
    Interpreter(loadModelFile(context, "model.tflite")).use { interpreter ->
        val output = Array(1) { FloatArray(1) }
        interpreter.run(inputData, output)
        return output[0][0]
    }
}
