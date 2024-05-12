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
            val builder : AuthorizationRequest.Builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri);
            builder.setScopes(arrayOf("streaming","user-modify-playback-state","user-read-private", "playlist-read", "playlist-read-private",))
            val request: AuthorizationRequest = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
            //val intent = Intent(this, SpotifySwipeMusic::class.java)
            //startActivity(intent)


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
                    Log.e("basari","basari")
                    val playlistId = "6u36F4hdBHjNi8AB38fuhf"
                        Log.e("sifre",AuthorizationResponse.Type.TOKEN.toString())
                        val token = "Bearer BQBhygxAdHNFtaMIW1a4yNsUqoZiANVcKhUbdih_oY2LY_TlZgd_WKTji5MqdtF2XaZEC-zNtQn-Tk6IGxR6IhrFIcrz-c3oxhxEN-vmWR95JPlX2gc"
                        CoroutineScope(Dispatchers.IO).launch{
                            try {
                                val pause =  spotifyApi.service.pause(token)
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

                // Auth flow returned an error
                AuthorizationResponse.Type.ERROR -> {
                    // Handle error response
                    Log.e("hata","hata")

                }

                // Most likely auth flow was cancelled
                else -> {
                    // Handle other cases
                }
            }
        }
    }

}