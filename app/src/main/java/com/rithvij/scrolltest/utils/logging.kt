package com.rithvij.scrolltest.utils

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

fun appendLog(text: String, filename: String, mode: Mode) {
    val logFile = File("sdcard/$filename")
    if (!logFile.exists()) {
        try {
            logFile.createNewFile()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
    try {
        //BufferedWriter for performance, true to set append to file flag
        val append = (mode == Mode.Append)
        val buf = BufferedWriter(FileWriter(logFile, append))
        buf.append(text)
        buf.newLine()
        buf.close()
    } catch (e: IOException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }

}
