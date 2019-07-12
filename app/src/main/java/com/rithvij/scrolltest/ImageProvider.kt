package com.rithvij.scrolltest

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.TypedValue
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rithvij.scrolltest.utils.copyFile
import java.io.File
import java.io.FileOutputStream

class ImageProvider {

    fun createTempFile(context: Context, resourceId: Int) : File {
        //              https://stackoverflow.com/a/5834643/8608146
        val value = TypedValue()
        context.resources.getValue(resourceId, value, true)
        Log.d("main", "Resource filename: ${value.string}")
//

        val inputStream = context.resources.openRawResource(resourceId)
        val tempFile = File.createTempFile("image", ".jpg")
        copyFile(inputStream, FileOutputStream(tempFile))

        println(tempFile.absolutePath)

        return tempFile
    }

    fun createTempFile(context: Context, url: String?) : File {
        lateinit var x : File
        Glide.with(context).asFile().load(url).into(object : CustomTarget<File>(){
            override fun onLoadCleared(placeholder: Drawable?) {
                println("onLoad cleared")
            }
            override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                Log.d("ImageProvider", resource.path)
                x = resource
            }
        })
        println("returning from the function")
        return x
    }

}
