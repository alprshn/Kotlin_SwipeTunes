package com.example.kotlin_spotify_random_like_app

import PlaylistRequest
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePlayList(private val context: Context, private val spotifyApi: SpotifyApi, private val accessToken: String) {
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences
    companion object {
        private const val PREFS_NAME = "SpotifyPrefs"
        private const val PLAYLIST_ID_KEY = "playlist_id"
    }
    fun create(){
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedPlaylistId = sharedPreferences.getString(PLAYLIST_ID_KEY, null)
        if (savedPlaylistId == null) {
        val playlistRequest = PlaylistRequest(
            "SwipeTunes",
            "Her kaydırışınızda keşfedeceğiniz yeni ve rastgele şarkılar! Bu liste, müzik zevkinizi genişletmek için oluşturulmuştur.",
            false)

        val token = "Bearer ${accessToken.toString()}"

        CoroutineScope(Dispatchers.IO).launch{
            try {
                val userID = spotifyApi.service.userID(token)
                Log.e("userID",userID.id.toString())

                val createPlayListID = spotifyApi.service.createPlaylist(userID.id, token, playlistRequest)
                createPlayListID
                sharedPreferences.edit().putString(PLAYLIST_ID_KEY, createPlayListID.id).apply()
            }
            catch (e: Exception) {
                Log.e("deneme", "Error: ${e.message}")
            }
        }
        } else {
            Log.e("Playlist", "Existing Playlist ID: $savedPlaylistId")
            // savedPlaylistId ile işlemler yapabilirsiniz
        }
    }
}