package com.rithvij.scrolltest.utils

import android.content.Context

fun loadJSONFromAsset(applicationContext: Context, fileName: String): String? {
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