package com.example.kotlin_spotify_random_like_app

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import java.text.FieldPosition

class StartedScreenActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var dotsLayout: LinearLayout
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var dots: Array<TextView>
    private val clientId = "1e6d0591bbb64af286b323ff7d26ce0f"
    private val redirectUri = "http://com.example.kotlin_spotify_random_like_app/callback"
    private val REQUEST_CODE = 1337
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
        viewPager.addOnPageChangeListener(changeListener) // changeListener'ı burada ekliyoruz

        addDots(0)

        var deneme: Button = findViewById(R.id.loginButton)
        deneme.setOnClickListener {
            val builder : AuthorizationRequest.Builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri);
            builder.setScopes(arrayOf("streaming","user-modify-playback-state","user-read-private", "playlist-read", "playlist-read-private","playlist-modify-private","playlist-modify-public","user-read-email","user-read-recently-played","user-read-currently-playing"))
            val request: AuthorizationRequest = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
        }

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

        }

        override fun onPageSelected(position: Int) {
            addDots(position)
        }

        override fun onPageScrollStateChanged(state: Int) {

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    //accessToken = response.accessToken
                    SpotifyApiManager.accessToken = "Bearer ${response.accessToken}"
                    //SpotifyApiManager.getNewTrackAndAddToList()
                    // viewModel.setAccessToken("Bearer $accessToken")
                    // val createPlayList = CreatePlayList(this, spotifyApi, accessToken.toString())
                    //createPlayList.create()
                    //val intent = Intent(this, SpotifySwipeMusic::class.java)
                    // startActivity(intent)
                    //Log.e("accestoken", accessToken.toString())
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("hata","hata")
                }
                else -> {
                }
            }
        }
    }



    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}