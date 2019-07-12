package com.rithvij.scrolltest

import java.io.File
import android.Manifest
import android.os.Build
import android.os.Bundle
import java.io.IOException
import com.google.gson.Gson
import android.widget.Button
import android.graphics.Color
import android.content.Intent
import java.io.FileOutputStream
import android.app.WallpaperManager
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.FileProvider
import androidx.viewpager.widget.ViewPager
import androidx.core.content.ContextCompat
import com.rithvij.scrolltest.utils.copyFile
import androidx.appcompat.app.AppCompatActivity
import com.rithvij.scrolltest.utils.loadJSONFromAsset
import com.rithvij.scrolltest.utils.convertFileToContentUri

const val EXTERNAL_ST_PERMISSION = 2423

class MainActivity : AppCompatActivity() {

    private var currentPage: Int = 0
    private lateinit var viewPager: ViewPager
    private lateinit var dock: Button
    private lateinit var adapter: HomePagesAdapter
    private lateinit var pageModels: MutableList<PageModel>
    private lateinit var wallpaperManager: WallpaperManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wallpaperManager = WallpaperManager.getInstance(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }

        val data = loadJSONFromAsset(applicationContext, "data.json")
        val gson = Gson()
        pageModels = mutableListOf()

        if (data != null) {
            val listData = gson.fromJson(data, Array<JsonPageModel>::class.java)
            pageModels.addAll(0, listData.map {
                return@map PageModel(
                    it.label,
                    resources.getIdentifier(
                        it.icon,
                        "drawable",
                        packageName
                    ), it.url
                )
            })
        } else {
            println("Shit it's null $data")
        }

        dock = findViewById(R.id.dock)
//        dock.text = pageModels[0].label
        adapter = HomePagesAdapter(pageModels.toList(), this)

        viewPager = findViewById(R.id.pages)
        viewPager.adapter = adapter

        dock.setOnClickListener {
            requestPermissions()
            val path = filesDir.absolutePath
            println(path)
            try {
//                https://stackoverflow.com/a/32038517/8608146
//                val inputStream = resources.openRawResource(+ R.drawable.orange)
                val inputStream = resources.openRawResource(pageModels[currentPage].icon)
                val tempFile = File.createTempFile("image", ".jpg")
                copyFile(inputStream, FileOutputStream(tempFile))

                println(tempFile.absolutePath)
                val myIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    wallpaperManager.getCropAndSetWallpaperIntent(convertFileToContentUri(applicationContext, tempFile))
                } else {
                    TODO("VERSION.SDK_INT < KITKAT")
                }
//                val myIntent = Intent(Intent.ACTION_VIEW)
                myIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                val uri = FileProvider.getUriForFile(
                    this@MainActivity,
                    BuildConfig.APPLICATION_ID + ".provider",
                    tempFile
                )
                myIntent.data = uri
                startActivity(myIntent)
            } catch (e: IOException) {
                throw RuntimeException("Can't create temp file ", e)
            }

//            getUriFromResource(applicationContext, pageModels[currentPage].icon)
//            val file = File(obbDir, "y.png")
//            val pathx = file.absolutePath // get absolute path
//            println(pathx)
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
        }
    }
}
