package com.rithvij.scrolltest.utils

import com.rithvij.scrolltest.config.APPDir
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

fun appendLog(text: String, filename: String, logMode: Logmode) {
    val logFile = File("$APPDir/logs/$filename")
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
        val append = (logMode == Logmode.Append)
        val buf = BufferedWriter(FileWriter(logFile, append))
        buf.append(text)
        buf.newLine()
        buf.close()
    } catch (e: IOException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }

}
