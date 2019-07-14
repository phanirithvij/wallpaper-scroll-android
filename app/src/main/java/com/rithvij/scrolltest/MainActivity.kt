package com.rithvij.scrolltest

import java.io.File
import java.util.Date
import android.net.Uri
import android.Manifest
import android.os.Build
import android.os.Bundle
import java.io.IOException
import com.google.gson.Gson
import android.widget.Button
import android.graphics.Color
import android.content.Intent
import android.app.WallpaperManager
import android.graphics.BitmapFactory
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.FileProvider
import androidx.viewpager.widget.ViewPager
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.rithvij.scrolltest.config.APPDir
import com.rithvij.scrolltest.utils.*
import com.rithvij.scrolltest.models.*
import java.text.DateFormat

const val EXTERNAL_ST_PERMISSION = 2423

class MainActivity : AppCompatActivity() {

    private var currentPage: Int = 0
    private lateinit var viewPager: ViewPager
    private lateinit var dock: Button
    private lateinit var adapter: HomePagesAdapter
    private lateinit var pageModels: MutableList<PageModel>
    private lateinit var wallpaperManager: WallpaperManager

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppDir()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                EXTERNAL_ST_PERMISSION
            )
        }
        wallpaperManager = WallpaperManager.getInstance(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }

        loadJsonData()

        dock = findViewById(R.id.dock)
//        dock.text = pageModels[0].label
        adapter = HomePagesAdapter(pageModels.toList(), this)

        viewPager = findViewById(R.id.pages)
        viewPager.adapter = adapter

        dock.setOnClickListener {
            requestPermissions()
        }

//        viewPager.setPadding(130, 0, 130, 0) // cool effect
//        https://stackoverflow.com/a/45513456/8608146
        with(viewPager) {
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                //                https://bit.ly/32gnXsh
                override fun onPageScrollStateChanged(state: Int) {
                    when (state) {
                        ViewPager.SCROLL_STATE_DRAGGING -> {
                            println("Drag scroll $state")
                        }
                        ViewPager.SCROLL_STATE_IDLE -> {
                            println("Idle scroll $state")
                        }
                        ViewPager.SCROLL_STATE_SETTLING -> {
                            println("Scroll settled or stopped $state")
                        }
                    }
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    val xOffset = (position + positionOffset) / (pageModels.size - 1)
//                    println(xOffset)
                    wallpaperManager.setWallpaperOffsets(viewPager.windowToken, xOffset, 0.0f)
                }

                override fun onPageSelected(position: Int) {
//                    dock.text = pageModels[position].label
                    currentPage = position
                    println("Page selected $position")
                    println("Current Item $currentItem")
                }
            })
        }
    }

    private fun loadJsonData(){
//        val data = loadAssetContent(applicationContext, "data.json")
        val data = loadFileContent(applicationContext, "$APPDir/data/data.json")
        val format = DateFormat.getDateInstance()
        val ran = Date()
        val text = format.format(ran)
        appendLog(data!!, "data-latest.json", Logmode.Erase)
        appendLog(text, "data-latest.json", Logmode.Append)
        val gson = Gson()
        pageModels = mutableListOf()

//        println(filesDir)
//        println(getExternalFilesDir("Data"))

        try {
            val listData = gson.fromJson(data, Array<JsonPageModel>::class.java)
            pageModels.addAll(0, listData.map {
                var icon : Int? = null
                if (it.resource != null){
                    icon = resources.getIdentifier(
                        it.resource,
                        "drawable",
                        packageName
                    )
                }
//                Toast.makeText(this, "${it.label}", Toast.LENGTH_SHORT).show()
                appendLog("${it.resource} ${it.label} ${it.url}", "labels.txt", Logmode.Append)
                return@map PageModel(
                    it.label,
                    icon,
                    it.url
                )
            })
        } catch (e: NullPointerException){
            println("FUCK")
            e.printStackTrace()
        }
    }

    private fun cleanUpImage(content: Uri){
        // The images are being stored in Pictures/ directory
        // Delete them right after we use them
//        https://stackoverflow.com/a/3414749/8608146
        val filepath = (getRealPathFromURI(applicationContext, content))
        deleteImage(applicationContext, content.path!!)
        deleteImage(applicationContext, filepath)
    }

    private fun requestPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                EXTERNAL_ST_PERMISSION
            )
        } else {
            val path = filesDir.absolutePath
            println(path)
            try {
//                https://stackoverflow.com/a/32038517/8608146
//                val inputStream = resources.openRawResource(+ R.drawable.orange)

                val tempFile : File? = ImageProvider(applicationContext, pageModels[currentPage]).getTempFile()
                println(tempFile!!.path)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    val content = convertFileToContentUri(applicationContext, tempFile)
                    val myIntent = wallpaperManager.getCropAndSetWallpaperIntent(content)
//                val myIntent = Intent(Intent.ACTION_VIEW)
                    myIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    val uri = FileProvider.getUriForFile(
                        this@MainActivity,
//                        BuildConfig.APPLICATION_ID + ".provider",
                        applicationContext.packageName + ".provider",
                        tempFile
                    )
                    myIntent.data = uri
                    startActivity(myIntent)
                    cleanUpImage(content)
                } else {
                    wallpaperManager.setBitmap(BitmapFactory.decodeFile(tempFile.path))
                }
            } catch (e: IOException) {
                throw RuntimeException("Can't create temp file ", e)
            }

//            getUriFromResource(applicationContext, pageModels[currentPage].resource)
//            val file = File(obbDir, "y.png")
//            val pathx = file.absolutePath // get absolute path
//            println(pathx)

        }
    }
}
