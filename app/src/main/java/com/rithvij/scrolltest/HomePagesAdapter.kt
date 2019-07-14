package com.rithvij.scrolltest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rithvij.scrolltest.models.PageModel

class HomePagesAdapter(
    private var pageModels: List<PageModel>,
    private var context: Context
) : PagerAdapter() {

    private lateinit var layoutInflater: LayoutInflater
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return pageModels.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.page, container, false)
        val imageView = view.findViewById<ImageView>(R.id.image_view)
        if (pageModels[position].url != null) {
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.animated_loader)
                .error(R.drawable.im404)
            Glide
                .with(context)
                .applyDefaultRequestOptions(requestOptions)
                .load(pageModels[position].url)
                .into(imageView)
        } else {
            Glide
                .with(context)
                .load(pageModels[position].resource)
                .thumbnail(0.1f)
                .into(imageView)
        }
        val textView = view.findViewById<TextView>(R.id.text_view)
        textView.text = pageModels[position].label
        container.addView(view, 0)
        println("adding... $view")
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
        println("removing... $`object`")
//        https://stackoverflow.com/a/26654608/8608146
    }
}
