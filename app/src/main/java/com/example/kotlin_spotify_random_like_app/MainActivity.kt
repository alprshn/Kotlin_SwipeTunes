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
   // private lateinit var spotifyService: SpotifyService

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        spotifyAuth = SpotifyConnection(this)
        spotifyApi = SpotifyApi // SpotifyApi nesnesini başlatın



        binding.button.setOnClickListener{
            //val intent = Intent(this, SpotifySwipeMusic::class.java)
            //startActivity(intent)
            val playlistId = "6u36F4hdBHjNi8AB38fuhf"
            val token = "Bearer BQCBgk0nErRIM28bnXu9MYQvCz7s4D7Wn8YankpmEt6lRy3DavwqcPHCSfXp9t1ErXi4wWSKH5FQnwd8aTJgwh26S9ToYxkwB3lTuACY6kv9JFpe76Y"

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
    }

    override fun onStart() {
        super.onStart()
        spotifyAuth?.connectionStart()

    }

    override fun onStop() {
        super.onStop()

    }

}