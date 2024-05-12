package com.example.kotlin_spotify_random_like_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_spotify_random_like_app.databinding.ActivityMainBinding
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var spotifyAuth: SpotifyConnection
    private lateinit var spotifyApi: SpotifyApi

    private val clientId = "1e6d0591bbb64af286b323ff7d26ce0f"
    private val redirectUri = "http://com.example.kotlin_spotify_random_like_app/callback"
    private val REQUEST_CODE = 1337

   // private lateinit var spotifyService: SpotifyService

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        spotifyAuth = SpotifyConnection(this)
        spotifyApi = SpotifyApi // SpotifyApi nesnesini başlatın



        binding.button.setOnClickListener{
            var builder : AuthorizationRequest.Builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri);
            builder.setScopes(arrayOf("user-modify-playback-state"))
            var request: AuthorizationRequest = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
            //val intent = Intent(this, SpotifySwipeMusic::class.java)
            //startActivity(intent)
            val playlistId = "6u36F4hdBHjNi8AB38fuhf"
            val token = "Bearer BQB7yUwlOyxC3kGDvVzv_r6om6JvsflGsorUw3tZFRgEqXgkoKPnH0p-FZfd5F3P8mCwNGD3wNig_vfIKOuE1uapSTnoIMSo64ppfqaMwTgKQSEH4zg"




        }
    }

    override fun onStart() {
        super.onStart()
        spotifyAuth?.connectionStart()

    }

    override fun onStop() {
        super.onStop()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)

            when (response.type) {
                // Response was successful and contains auth token
                AuthorizationResponse.Type.TOKEN -> {
                    // Handle successful response
                }

                // Auth flow returned an error
                AuthorizationResponse.Type.ERROR -> {
                    // Handle error response
                }

                // Most likely auth flow was cancelled
                else -> {
                    // Handle other cases
                }
            }
        }
    }

}