package com.example.kotlin_spotify_random_like_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private var accessToken: String? = null // accessToken değişkenini tanımladık

   // private lateinit var spotifyService: SpotifyService

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        spotifyAuth = SpotifyConnection(this)
        spotifyApi = SpotifyApi // SpotifyApi nesnesini başlatın

        binding.button.setOnClickListener{
            val builder : AuthorizationRequest.Builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri);
            builder.setScopes(arrayOf("streaming","user-modify-playback-state","user-read-private", "playlist-read", "playlist-read-private",))
            val request: AuthorizationRequest = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
            //val intent = Intent(this, SpotifySwipeMusic::class.java)
            //startActivity(intent)
        }


        binding.button2.setOnClickListener {
            val token = "Bearer ${accessToken.toString()}"
            Log.e("basari",accessToken.toString())
            val playlistId = "6u36F4hdBHjNi8AB38fuhf"

            CoroutineScope(Dispatchers.IO).launch{
                try {
                    AuthorizationResponse.Type.CODE
                    spotifyApi.service.pause(token)
                }
                catch (e: Exception) {
                    Log.e("deneme", "Error: ${e.message}")
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val playlist = spotifyApi.service.getPlaylist(playlistId, token)
                    Log.e("deneme", "Playlist Name: ${playlist.name}")
                    Log.e("deneme", "Playlist Name: ${playlist.followers}")
                    Log.e("deneme", "Playlist Name: ${playlist.owner.display_name}")
                } catch (e: Exception) {
                    Log.e("deneme", "Error: ${e.message}")
                }
            }
        }

        binding.button3.setOnClickListener {
            val token = "Bearer ${accessToken.toString()}"
                CoroutineScope(Dispatchers.IO).launch{
                    try {
                        spotifyApi.service.next(token)
                    }
                    catch (e: Exception) {
                        Log.e("deneme", "Error: ${e.message}")
                    }
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    accessToken = response.accessToken
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("hata","hata")
                }
                else -> {
                }
            }
        }
    }


    fun getASong(){
        val randomSeed = generateQuery(2)
        val randomOffset = (Math.random() * 20).toInt() // returns a random Integer from 0 to 20

    }

    private fun generateQuery( length: Int): String{
        var result = ""
        var characters = "abcdefghijklmnopqrstuvwxyz"
        var charactersLength = characters.length
        for ( i in 0 until length){
            val randomIndex = (Math.random() * charactersLength).toInt()
            result += characters[Math.floor(Math.random() * charactersLength).toInt()]
        }
        return result.toString()
    }

    private fun play(){

    }

}