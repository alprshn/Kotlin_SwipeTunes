package com.example.kotlin_spotify_random_like_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_TIMER: Long = 3000
    private val splashName= "SplashPrefs"
    private val splashFirst= "FirstLogin"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkFirstTimeLaunch()


    }

    private fun checkFirstTimeLaunch() {
        val sharedPref = getSharedPreferences(splashName, MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean(splashFirst, false)
        if (!isFirstTime) {
            startStartedActivity()
            finish()
            return
        }
        else{
            val handler = Handler(Looper.getMainLooper())
// Runnable bloğunu lambda olarak tanımlayarak 3000 ms (3 saniye) sonra çalışacak şekilde ayarlayalım.
            handler.postDelayed({
                startMainActivity()
            }, SPLASH_TIMER)
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun startStartedActivity() {
        val intent = Intent(this, StartedScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}