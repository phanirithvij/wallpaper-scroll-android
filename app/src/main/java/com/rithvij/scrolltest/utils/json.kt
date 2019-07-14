package com.rithvij.scrolltest.utils

import android.content.Context
import java.io.File

fun loadAssetContent(applicationContext: Context, fileName: String): String? {
    val json: String?
    try {
        val inpFile = applicationContext.assets.open(fileName)
        val size = inpFile.available()
        val buffer = ByteArray(size)
        inpFile.read(buffer)
        inpFile.close()
        json = String(buffer, Charsets.UTF_8)
    } catch (ex: Exception) {
        ex.printStackTrace()
        return null
    }
    return json
}

fun loadFileContent(context: Context, fileName: String): String? {
    val json: String?
    try {
        val inpFile = File(fileName)
        if (!inpFile.exists()){
            inpFile.createNewFile()
            val data = context.assets.open("data.json")
            copyFile(data, inpFile.outputStream())
        }
        json = inpFile.readText(Charsets.UTF_8)
    } catch (ex: Exception) {
        ex.printStackTrace()
        return null
    }
    return json
}
