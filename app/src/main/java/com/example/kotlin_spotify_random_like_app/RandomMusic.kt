package com.example.kotlin_spotify_random_like_app

import Offset
import PlayRequest
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RandoMusic(private val spotifyApi: SpotifyApi, private val accessToken: String) {

    fun getASong() {
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
                    play(trackUri, randomOffset)
                } else {
                    Log.e("Error", "No tracks found.")
                }
            } catch (e: Exception) {
                Log.e("Error", "Error: ${e.message}")
            }
        }
    }

    private fun play( trackUri: String, offset: Int){
        Log.e("Random Offset", offset.toString())

        if (accessToken.isNullOrEmpty()) {
            Log.e("Error", "Access token is null or empty.")
            return
        }

        val token = "Bearer $accessToken"
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