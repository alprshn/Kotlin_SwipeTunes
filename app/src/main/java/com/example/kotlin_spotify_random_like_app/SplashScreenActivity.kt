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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val handler = Handler(Looper.getMainLooper())
// Runnable bloğunu lambda olarak tanımlayarak 3000 ms (3 saniye) sonra çalışacak şekilde ayarlayalım.
        handler.postDelayed({
            val intent = Intent(this, StartedScreenActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIMER)
    }
}