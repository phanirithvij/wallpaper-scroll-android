package com.rithvij.scrolltest

import android.os.Build
import android.os.Bundle
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import android.app.WallpaperManager

class MainActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    private lateinit var adapter: HomePagesAdapter
    private lateinit var pageModels : List<PageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }

        pageModels = List(9) { PageModel("",1) }
        pageModels[0].icon = R.drawable.allanime
        pageModels[0].label = "Anime for Weebs"
        pageModels[1].icon = R.drawable.painterboy
        pageModels[1].label = "Painter Boy wazzup my man"
        pageModels[2].icon = R.drawable.space
        pageModels[2].label = "Space adventures"
        pageModels[3].icon = R.drawable.doctorstrange
        pageModels[3].label = "A spell for an eye"
        pageModels[4].icon = R.drawable.colors
        pageModels[4].label = "Colorful looking thing"
        pageModels[5].icon = R.drawable.green
        pageModels[5].label = "Hmmm those trees have green in 'em"
        pageModels[6].icon = R.drawable.orange
        pageModels[6].label = "Half dead clock looking thing"
        pageModels[7].icon = R.drawable.luffy
        pageModels[7].label = "I'm gonna be the king of the pirates"
        pageModels[8].icon = R.drawable.deadpoop
        pageModels[8].label = "I can grow my balls back unlike Wolverine"

        adapter = HomePagesAdapter(pageModels, this)

        viewPager = findViewById(R.id.pages)
        viewPager.adapter = adapter
//        viewPager.setPadding(130, 0, 130, 0) // cool effect
//        https://stackoverflow.com/a/45513456/8608146
         with(viewPager) {
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
//                https://bit.ly/32gnXsh
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    val xOffset = (position + positionOffset) / (pageModels.size - 1)
//                    println(xOffset)
                    val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                    wallpaperManager.setWallpaperOffsets(viewPager.windowToken, xOffset, 0.0f)
                }

                override fun onPageSelected(position: Int) {}
            })

        }
    }
}
