package com.rithvij.scrolltest.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File


fun getUriFromResource(context: Context, resourceId: Int): Uri? {
//    https://stackoverflow.com/a/38340580/8608146
    val resources = context.resources
    val uri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(resourceId))
        .appendPath(resources.getResourceTypeName(resourceId))
        .appendPath(resources.getResourceEntryName(resourceId))
        .build()
    println(uri)
    return uri
}

@Throws(Exception::class)
fun convertFileToContentUri(context: Context, file: File): Uri {

    //Uri localImageUri = Uri.fromFile(localImageFile); // Not suitable as it's not a content Uri

    val cr = context.contentResolver
    val imagePath = file.absolutePath
    val imageName: String? = null
    val imageDescription: String? = null
    val uriString = MediaStore.Images.Media.insertImage(cr, imagePath, imageName, imageDescription)
    return Uri.parse(uriString)
}
