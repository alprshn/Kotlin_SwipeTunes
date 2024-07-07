package com.example.kotlin_spotify_random_like_app.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.kotlin_spotify_random_like_app.util.NetworkManager
import com.example.kotlin_spotify_random_like_app.R
import com.example.kotlin_spotify_random_like_app.repository.SpotifyApiManager
import com.example.kotlin_spotify_random_like_app.util.SpotifyConnectionManager
import com.example.kotlin_spotify_random_like_app.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ramotion.circlemenu.CircleMenuView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var spotifyConnectionManager: SpotifyConnectionManager
    private val prefsName = "AppPrefs"
    private val splashName= "SplashPrefs"
    private val splashFirst= "FirstLogin"
    private val firstTimeKey = "FirstTime"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        spotifyConnectionManager = SpotifyConnectionManager(this)
        spotifyConnectionManager.connectionStart()

        initNetworkDialog()
        animateBackground()

        saveRefreshToken()

        saveAccessToken()
        setupButtons()
        Log.e("denemeRefreshToken", SpotifyApiManager.refreshToken.toString())
        circularMenu()

    }
    private fun setupButtons() {
        binding.button4.setOnClickListener {
            // Open Spotify swipe music activity
            val intent = Intent(this, SpotifySwipeMusicActivity::class.java)
            startActivity(intent)
        }

    }

    private fun saveRefreshToken(){ //Burada sharedPreferences'a refreshToken'i ekledik
        val sharedPreferences:SharedPreferences = getSharedPreferences("prefToken", MODE_PRIVATE)
        val refreshToken: SharedPreferences.Editor = sharedPreferences.edit()
        refreshToken.putString("refresh_token", SpotifyApiManager.refreshToken).apply()
    }

    private fun saveAccessToken(){ //Burada sharedPreferences'a refreshToken'i ekledik
        val sharedPreferences:SharedPreferences = getSharedPreferences("prefAccessToken", MODE_PRIVATE)
        val refreshToken: SharedPreferences.Editor = sharedPreferences.edit()
        refreshToken.putString("access_token", SpotifyApiManager.accessToken).apply()
    }

    private fun animateBackground() {
        val constraintLayout:ConstraintLayout = binding.mainLayout
        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(1000)
        animationDrawable.setExitFadeDuration(2000)
        animationDrawable.start()
    }


    private fun circularMenu(){
        val circleMenu = binding.circleMenu

        circleMenu.setEventListener(object : CircleMenuView.EventListener() {
            override fun onMenuOpenAnimationStart(view: CircleMenuView) {

            }
            override fun onButtonClickAnimationStart(view: CircleMenuView, buttonIndex: Int) {
                super.onButtonClickAnimationStart(view, buttonIndex)
                when (buttonIndex) {
                    0 -> {
                        Toast.makeText(this@MainActivity, "Home Button Clicked", Toast.LENGTH_SHORT).show()
                                        }
                    1 -> {
                        signOut()
                        Toast.makeText(this@MainActivity, "Sign Out Button Clicked", Toast.LENGTH_SHORT).show()
                        // Diğer işlemler burada
                    }
                }
            }
        })
    }

    private fun signOut() {
        val splashSharedPref = getSharedPreferences(splashName, MODE_PRIVATE)
        splashSharedPref.edit().putBoolean(splashFirst, false).apply()
        val sharedPref = getSharedPreferences(prefsName, MODE_PRIVATE)
        sharedPref.edit().putBoolean(firstTimeKey, true).apply()
        spotifyConnectionManager.disconnect()
        startStartedActivity()
    }

    private fun startStartedActivity() {
        val intent = Intent(this, StartScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun initNetworkDialog() {
        val dialog = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
            .setView(R.layout.custom_dialog)
            .setCancelable(false)
            .create()

        val networkManager = NetworkManager(this)
        networkManager.observe(this) {
            if (!it) {
                if (!dialog.isShowing) {
                    dialog.show()
                }
            } else {
                if (dialog.isShowing) {
                    dialog.dismiss()  // 'hide' yerine 'dismiss' kullanmak genellikle daha doğru bir yaklaşımdır.
                }
            }
        }
    }

}