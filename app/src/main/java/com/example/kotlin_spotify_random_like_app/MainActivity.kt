package com.example.kotlin_spotify_random_like_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spotify.android.appremote.api.SpotifyAppRemote

class MainActivity : AppCompatActivity() {

    private val clientId = "your_client_id"
    private val redirectUri = "http://com.yourdomain.yourapp/callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null
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