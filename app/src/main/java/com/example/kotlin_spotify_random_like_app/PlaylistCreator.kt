package com.example.kotlin_spotify_random_like_app

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.kotlin_spotify_random_like_app.model.data.PlaylistRequest
import com.example.kotlin_spotify_random_like_app.model.remote.SpotifyApiBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistCreator(private val context: Context, private val spotifyApi: SpotifyApiBuilder, private val accessToken: String) {
    companion object {
        private const val PREFS_NAME = "SpotifyPrefs"
        private const val PLAYLIST_ID_KEY = "playlist_id"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun create() {
        val savedPlaylistId = getSavedPlaylistId()
        Log.e("savedPlaylistId", savedPlaylistId.toString())

        if (savedPlaylistId == null) {
            val playlistRequest = createPlaylistRequest()
            val authToken = "Bearer $accessToken"

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userIdResponse = spotifyApi.service.getUserProfile(authToken)
                    Log.e("userID", userIdResponse.id.toString())

                    val createdPlaylistResponse = spotifyApi.service.createPlaylist(userIdResponse.id, authToken, playlistRequest)
                    savePlaylistId(createdPlaylistResponse.id)
                } catch (e: Exception) {
                    Log.e("CreatePlayList", "Error: ${e.message}")
                }
            }
        } else {
            Log.e("Playlist", "Existing Playlist ID: $savedPlaylistId")
            // savedPlaylistId ile işlemler yapabilirsiniz
        }
    }

    private fun createPlaylistRequest(): PlaylistRequest {
        return PlaylistRequest(
            "SwipeTunes",
            "Her kaydırışınızda keşfedeceğiniz yeni ve rastgele şarkılar! Bu liste, müzik zevkinizi genişletmek için oluşturulmuştur.",
            false
        )
    }


    private fun getSavedPlaylistId(): String? {
        return sharedPreferences.getString(PLAYLIST_ID_KEY, null)
    }

    private fun savePlaylistId(id: String) {
        sharedPreferences.edit().putString(PLAYLIST_ID_KEY, id).apply()
    }
}