package com.rithvij.scrolltest

import java.io.File
import android.util.Log
import android.content.Context
import android.os.AsyncTask
import android.util.TypedValue
import java.io.FileOutputStream
import com.bumptech.glide.Glide
import com.google.common.io.Files
import com.rithvij.scrolltest.utils.copyFile
import com.bumptech.glide.request.FutureTarget
import com.rithvij.scrolltest.models.PageModel

interface AsyncResponse {
    fun processFinish(output: File?)
}

class MyAsyncTask(delegate: AsyncResponse) : AsyncTask<FutureTarget<File>, Void, File?>() {
    override fun doInBackground(vararg p0: FutureTarget<File>?): File? {
        return p0[0]?.get()
    }

    private var delegate: AsyncResponse? = null

    init {
        this.delegate = delegate
    }

    override fun onPostExecute(result: File?) {
        delegate!!.processFinish(result)
    }
}

class ImageProvider(private val context: Context, private val pageModel: PageModel) {

    fun getTempFile(): File? {
        return if (pageModel.url == null) {
            createTempFile(context, pageModel.resource!!)
        } else {
            createTempFile(context, pageModel.url)
        }
    }

    @Suppress("UnstableApiUsage")
    private fun createTempFile(context: Context, resourceId: Int): File? {
        //              https://stackoverflow.com/a/5834643/8608146
        val value = TypedValue()
        context.resources.getValue(resourceId, value, true)
        Log.d("main", "Resource filename: ${value.string}")

//        https://stackoverflow.com/a/15985486/8608146
        val ext = Files.getFileExtension(value.string.toString())

        val inputStream = context.resources.openRawResource(resourceId)
        val tempFile = File.createTempFile("image", ".$ext")
        copyFile(inputStream, FileOutputStream(tempFile))

        println(tempFile.absolutePath)

        return tempFile
    }


    private fun createTempFile(context: Context, url: String?): File? {
        val data = Glide.with(context).asFile().load(url).submit()
        val asyncTask = MyAsyncTask(object : AsyncResponse {
            override fun processFinish(output: File?) {
                println(output?.path)
            }
        }).execute(data)
        val file = asyncTask.get()
//        file!!.renameTo()
        val tempFile = File.createTempFile("image", ".png")
        copyFile(file!!.inputStream(), FileOutputStream(tempFile))
        return tempFile
    }

}
