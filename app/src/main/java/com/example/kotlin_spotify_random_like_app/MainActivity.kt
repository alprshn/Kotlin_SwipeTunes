package com.example.kotlin_spotify_random_like_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_spotify_random_like_app.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var spotifyAuth: SpotifyConnection
    private lateinit var spotifyApi: SpotifyApi
    private lateinit var spotifyService: SpotifyService

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        spotifyAuth = SpotifyConnection(this)

        binding.button.setOnClickListener{
            //val intent = Intent(this, SpotifySwipeMusic::class.java)
            //startActivity(intent)
            val spotifyService = spotifyApi.create()
            try {
                spotifyService.nec
                println("Next track command sent successfully!")
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }

        }
    }

    override fun onStart() {
        super.onStart()
        spotifyAuth?.connectionStart()

    }

    override fun onStop() {
        super.onStop()

    }

}