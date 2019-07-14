package com.rithvij.scrolltest.utils

import android.content.Context
import android.database.Cursor
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.File

fun getRealPathFromURI(context: Context, contentUri: Uri): String {
    var cursor: Cursor? = null
    try {
        val pro = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri, pro, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    } finally {
        cursor?.close()
    }
}

fun deleteImage(context: Context, filePath: String) {
    val fdelete = File(filePath)
    if (fdelete.exists()) {
        if (fdelete.delete()) {
            Log.e("-->", "file Deleted :$filePath")
            callBroadCast(context, filePath)
        } else {
            Log.e("-->", "file not Deleted :$filePath")
        }
    }
}

fun callBroadCast(context: Context, filePath: String) {
    MediaScannerConnection.scanFile(
        context,
        arrayOf(filePath),
        null
    ) { path, uri ->
        Log.e("ExternalStorage", "Scanned $path:")
        Log.e("ExternalStorage", "-> uri=$uri")
    }
}
