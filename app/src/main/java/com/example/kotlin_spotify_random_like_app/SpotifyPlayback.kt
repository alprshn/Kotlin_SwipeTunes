package com.example.kotlin_spotify_random_like_app

import AddTracksRequest
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpotifyPlayback(private val context: Context, private val spotifyApi: SpotifyApi, private val accessToken: String) {
    companion object {
        private const val PREFS_NAME = "SpotifyPrefs"
        private const val PLAYLIST_ID_KEY = "playlist_id"
    }
    fun addTrackToPlaylist(uri:String) {
        val playlistId = getPlaylistId()
        val token = "Bearer ${accessToken.toString()}"
        CoroutineScope(Dispatchers.IO).launch{
            try {

                val addTracksRequest = AddTracksRequest(listOf(uri),0)
                Log.e("addTracksRequest", addTracksRequest.toString())
                Log.e("playlistId", playlistId.toString())

                spotifyApi.service.addItemPlaylist(playlistId.toString(), token, addTracksRequest)
            }
            catch (e: Exception) {
                Log.e("deneme", "Error: ${e.message}")
            }
        }
    }



    fun getPlaylistId(): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PLAYLIST_ID_KEY, null)
    }
}