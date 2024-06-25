package com.example.kotlin_spotify_random_like_app

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide

class SliderAdapter(private val context:Context): PagerAdapter() {


    var images: ArrayList<Int> = arrayListOf(R.drawable.singerphoto,R.drawable.slide_1,R.drawable.slide_1,R.drawable.slide_1)
    var headings: ArrayList<Int> = arrayListOf(12,21,12,1)
    var Descriptions: ArrayList<Int> = arrayListOf(12,21,12,1)
    lateinit var layoutInflater:LayoutInflater
    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == (p1 as ConstraintLayout)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.slides_layout, container, false)
        val layout = view.findViewById<ConstraintLayout>(R.id.slideImageView)
        layout.setBackgroundResource(images[position])  // Background image set here

        //val headingText:TextView = view.findViewById(R.id.)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}