package com.example.kotlin_spotify_random_like_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onStart() {
        super.onStart()
    }
    private fun connected() {
        // Then we will write some more code here.
    }

    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
    }
}