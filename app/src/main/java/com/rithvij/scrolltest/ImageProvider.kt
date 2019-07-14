package com.rithvij.scrolltest

import android.content.Context
import android.os.AsyncTask
import android.util.TypedValue
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.google.common.io.Files
import com.rithvij.scrolltest.models.ImageType
import com.rithvij.scrolltest.models.PageModel
import com.rithvij.scrolltest.utils.copyFile
import java.io.File
import java.io.FileOutputStream

interface AsyncResponse {
    fun processFinish(output: File?)
}

class MyAsyncTask(delegate: AsyncResponse) : AsyncTask<FutureTarget<File>, Void, File?>() {
    override fun doInBackground(vararg p0: FutureTarget<File>?): File? {
        println("REACHED HERE")
        return p0[0]?.get()
    }

    override fun onProgressUpdate(vararg values: Void?) {
        println("Progress update $values")
        super.onProgressUpdate(*values)
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

    val type = ImageType.values()[pageModel.type]

    fun getImageFile(): File? {
//        return createTempFile(context)
        when (type) {
            ImageType.Resource -> {
                return createTempFile(context, pageModel.resource!!)
            }
            ImageType.Url -> {
                return createTempFile(context, pageModel.url)
            }
            ImageType.File -> {
                val file = File(pageModel.file!!)
                return if (file.exists()) {
                    file
                } else {
                    // no such file
                    createTempFile(context, R.drawable.im404)
                }
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun createTempFile(context: Context, resourceId: Int): File? {
//         AssetImage
//         https://stackoverflow.com/a/5834643/8608146
        val value = TypedValue()
        context.resources.getValue(resourceId, value, true)
//        Log.d("main", "Resource filename: ${value.string}")

//        https://stackoverflow.com/a/15985486/8608146
        val ext = Files.getFileExtension(value.string.toString())

        val inputStream = context.resources.openRawResource(resourceId)
        val tempFile = File.createTempFile("image", ".$ext")
        copyFile(inputStream, FileOutputStream(tempFile))

        println(tempFile.absolutePath)

        return tempFile
    }

    private fun createTempFile(context: Context, url: String?): File? {
//        NetworkImage
/*        val data : FutureTarget<File> = when (type) {
            ImageType.File -> {
                Glide.with(context).asFile().load(File(pageModel.file!!)).submit()
            }
            ImageType.Url -> {
                Glide.with(context).asFile().load(pageModel.url).submit()
            }
            ImageType.Resource -> {
                println("Loading res ${pageModel.resource}")
                Glide.with(context).asFile().load(pageModel.resource).submit()
            }
        }*/
        val data = Glide
            .with(context)
            .asFile()
            .load(url)
            .submit()
        val asyncTask = MyAsyncTask(object : AsyncResponse {
            override fun processFinish(output: File?) {
                println("${output?.path} output?.path")
            }
        }).execute(data)
        val file = asyncTask.get()
//        file!!.renameTo()
        val tempFile = File.createTempFile("image", ".png")
//        copyFile(file!!.inputStream(), FileOutputStream(tempFile))
        copyFile(file!!.inputStream(), tempFile.outputStream())
        return tempFile
    }

}
