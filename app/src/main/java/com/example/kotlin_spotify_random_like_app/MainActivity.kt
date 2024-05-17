package com.example.kotlin_spotify_random_like_app

import Offset
import PlayRequest
import PlaylistRequest
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
            builder.setScopes(arrayOf("streaming","user-modify-playback-state","user-read-private", "playlist-read", "playlist-read-private","playlist-modify-private","playlist-modify-public"))
            val request: AuthorizationRequest = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
            //val intent = Intent(this, SpotifySwipeMusic::class.java)
            //startActivity(intent)
        }


        binding.button5.setOnClickListener {

            val playlistRequest = PlaylistRequest(
                name = "New Playlist",
                description = "New playlist description",
                public = false
            )
            val userId = "smedjan"
            val token = "Bearer ${accessToken.toString()}"

            CoroutineScope(Dispatchers.IO).launch{
                try {
                    AuthorizationResponse.Type.CODE
                    val create = spotifyApi.service.createPlaylist(userId, token, playlistRequest)
                }
                catch (e: Exception) {
                    Log.e("deneme", "Error: ${e.message}")
                }
            }

        }

        binding.button4.setOnClickListener {
            getASong(accessToken.toString())
            //play()
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



    fun getASong(accessToken: String?) {
        if (accessToken.isNullOrEmpty()) {
            Log.e("Error", "Access token is null or empty.")
            return
        }
        val randomSeed = generateQuery(2)
        Log.e("Random Seed", randomSeed)
        val randomAlbum = (Math.random() * 19).toInt() // returns a random Integer from 0 to 20
        Log.e("Random Album",randomAlbum.toString())
        val token = "Bearer $accessToken"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = spotifyApi.service.searchAlbum("q=$randomSeed", token)
                if (response.albums.items.isNotEmpty()) {
                    val track = response.albums.items[randomAlbum]
                    val trackUri = track.uri
                    response.albums.items.forEachIndexed { index, tracks ->
                        val trackName = tracks.name
                        val trackUris = tracks.uri
                        Log.e("Track $index", "Name: $trackName, URI: $trackUris")
                    }
                    Log.e("Track Type", response.albums.total.toString())
                    Log.e("Track URI", response.albums.href)
                    val randomOffset = (Math.random() * (track.total_tracks-1)).toInt()
                    // Calling play function with the found track URI
                    play(accessToken, trackUri, randomOffset)
                } else {
                    Log.e("Error", "No tracks found.")
                }
            } catch (e: Exception) {
                Log.e("Error", "Error: ${e.message}")
            }
        }
    }


    private fun play(accessTokene: String?, trackUri: String, offset: Int){
        Log.e("Random Offset", offset.toString())

        if (accessTokene.isNullOrEmpty()) {
            Log.e("Error", "Access token is null or empty.")
            return
        }

        val token = "Bearer $accessTokene"
        val requestBody = PlayRequest(trackUri, Offset(offset), 0)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.e("Play Request", requestBody.toString())
                spotifyApi.service.play(requestBody, token)
            } catch (e: Exception) {
                Log.e("Error", "Error: ${e.message}")
            }
        }
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



}