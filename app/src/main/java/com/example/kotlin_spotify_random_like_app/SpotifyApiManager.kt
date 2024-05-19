package com.example.kotlin_spotify_random_like_app

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpotifyApiManager {
    private lateinit var spotifyApi: SpotifyApi

    fun initialize(api: SpotifyApi) {
        spotifyApi = api
    }

    fun pause(token:String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                spotifyApi.service.pause(token)
            } catch (e: Exception) {
                Log.e("SpotifyApiManager", "Error: ${e.message}")
            }
        }

    }

    fun next(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                spotifyApi.service.next(token)
            } catch (e: Exception) {
                Log.e("SpotifyApiManager", "Error: ${e.message}")
            }
        }
    }

}