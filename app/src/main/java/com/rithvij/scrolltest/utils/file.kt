package com.rithvij.scrolltest.utils

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@Throws(IOException::class)
fun copyFile(`in`: InputStream, out: OutputStream) {
//https://discuss.kotlinlang.org/t/kotlin-copy-file/7034/11
    val buffer = ByteArray(1024)
    var len = `in`.read(buffer)
    while (len > 0) {
        if (len != -1){
            out.write(buffer, 0, len)
            len = `in`.read(buffer)
        }
    }
    `in`.close()
    out.close()
}
