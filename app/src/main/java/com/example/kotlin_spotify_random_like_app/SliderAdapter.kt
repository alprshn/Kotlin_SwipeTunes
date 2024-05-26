package com.example.kotlin_spotify_random_like_app

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout

class SliderAdapter(private val context:Context): PagerAdapter() {


    var Images: ArrayList<Int> = arrayListOf(12,21,12,1)
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
        var view: View = layoutInflater.inflate(R.layout.slides_layout,container,false)
        container.addView(view)
        return super.instantiateItem(container, position)
    }
}