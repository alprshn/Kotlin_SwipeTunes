package com.example.kotlin_spotify_random_like_app

import android.os.Bundle
import android.text.Html
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import java.text.FieldPosition

class StartedScreenActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var dotsLayout: LinearLayout
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var dots: Array<TextView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_started_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager = findViewById(R.id.slider)
        dotsLayout = findViewById(R.id.dots)

        sliderAdapter = SliderAdapter(this)
        viewPager.adapter = sliderAdapter

        addDots(0)
        viewPager.addOnPageChangeListener(changeListener)
    }

    private fun addDots(position: Int){
        dots = Array(4) { TextView(this) }
        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i].text = Html.fromHtml("&#8226;")
            dots[i].textSize = 35f // Kullanılabilir bir textSize ayarı

            dotsLayout.addView(dots[i])
        }

        if (dots.size>0){
            dots[position].setTextColor(resources.getColor(com.google.android.material.R.color.design_default_color_primary_dark))
        }
    }

    val changeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            TODO("Not yet implemented")
        }

        override fun onPageSelected(position: Int) {
            addDots(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            TODO("Not yet implemented")
        }

    }



    }