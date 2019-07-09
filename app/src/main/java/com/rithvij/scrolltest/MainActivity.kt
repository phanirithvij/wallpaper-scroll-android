package com.rithvij.scrolltest

import android.os.Build
import android.os.Bundle
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import android.app.WallpaperManager
import android.widget.Button
import com.google.gson.Gson
import com.rithvij.scrolltest.utils.loadJSONFromAsset

class MainActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    private lateinit var dock: Button
    private lateinit var adapter: HomePagesAdapter
    private lateinit var pageModels: MutableList<PageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }

        val data = loadJSONFromAsset(applicationContext, "data.json")
        val gson = Gson()
        pageModels = mutableListOf()

        if (data != null){
            val listData = gson.fromJson(data, Array<JsonPageModel>::class.java)
            pageModels.addAll(0, listData.map {
                return@map PageModel(it.label, resources.getIdentifier(it.icon, "drawable", packageName), it.url)
            })
        } else {
            println("Shit it's null $data")
        }

        dock = findViewById(R.id.dock)
//        dock.text = pageModels[0].label
        adapter = HomePagesAdapter(pageModels.toList(), this)

        viewPager = findViewById(R.id.pages)
        viewPager.adapter = adapter

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
                    val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                    wallpaperManager.setWallpaperOffsets(viewPager.windowToken, xOffset, 0.0f)
                }

                override fun onPageSelected(position: Int) {
//                    dock.text = pageModels[position].label
                    println("Page selected $position")
                }
            })

        }
    }
}
