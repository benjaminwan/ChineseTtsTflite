package com.benjaminwan.compose.chinesettstflite.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * 把Asset中的文件[fileName]复制到对应目录[dirPath]
 * @param  fileName
 * @param  dirPath
 * @return 返回复制后的File
 */
fun copyAssetFileToDir(context: Context, fileName: String, dirPath: String): File? =
    copyAssetFileToDir(context, fileName, dirPath, fileName)

/**
 * 把Asset中的文件[sourceFileName]复制到对应目录[dirPath]并命名为[targetFileName]
 * @param  sourceFileName
 * @param  dirPath
 * @return 返回复制后的File
 */
fun copyAssetFileToDir(
    context: Context,
    sourceFileName: String,
    dirPath: String,
    targetFileName: String
): File? {
    try {
        val targetPath = FileUtils.getFileByPath(dirPath) ?: return null
        if (!FileUtils.createOrExistsDir(targetPath)) {
            return null
        }
        val targetFilePath = targetPath.absolutePath + File.separator + targetFileName
        val targetFile = File(targetFilePath)
        if (targetFile.exists()) {
            return targetFile
        } else {
            targetFile.createNewFile()
        }
        val fos = FileOutputStream(targetFile)
        val temp = ByteArray(1024)
        val inputStream = context.assets.open(sourceFileName)
        var i: Int
        i = inputStream.read(temp)
        while ((i) > 0) {
            fos.write(temp, 0, i)
            i = inputStream.read(temp)
        }
        fos.close()
        inputStream.close()
        return targetFile
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}