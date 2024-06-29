package com.example.kotlin_spotify_random_like_app

import PlaylistRequest
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePlayList(private val context: Context, private val spotifyApi: SpotifyApi, private val accessToken: String) {
    private var editor: SharedPreferences.Editor
    private var sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "SpotifyPrefs"
        private const val PLAYLIST_ID_KEY = "playlist_id"
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun create(){
        val savedPlaylistId = sharedPreferences.getString(PLAYLIST_ID_KEY, null)
        Log.e("savedPlaylistId",savedPlaylistId.toString())
        if (savedPlaylistId == null) {
            val playlistRequest = PlaylistRequest(
                "SwipeTunes",
                "Her kaydırışınızda keşfedeceğiniz yeni ve rastgele şarkılar! Bu liste, müzik zevkinizi genişletmek için oluşturulmuştur.",
                false
            )

            val token = "Bearer $accessToken"

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userID = spotifyApi.service.userID(token)
                    Log.e("userID", userID.id.toString())

                    val createPlayListID = spotifyApi.service.createPlaylist(userID.id, token, playlistRequest)
                    editor.putString(PLAYLIST_ID_KEY, createPlayListID.id).commit()
                } catch (e: Exception) {
                    Log.e("deneme", "Error: ${e.message}")
                }
            }
        } else {
            Log.e("Playlist", "Existing Playlist ID: $savedPlaylistId")
            // savedPlaylistId ile işlemler yapabilirsiniz
        }
    }
}