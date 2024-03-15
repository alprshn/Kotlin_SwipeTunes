package com.example.kotlin_spotify_random_like_app

import android.util.Log
import com.spotify.protocol.types.Track

class SpotifyConnectionTrack {
    private lateinit var spotifyConnection: SpotifyConnection
    var track: Track? = null

    fun connected() {
        spotifyConnection.spotifyAppRemote?.let {
            // Play a playlist
            val playlistURI = "spotify:playlist:3GhEaVPCWa7ZVTyis5WV54?si=79a14006b45f49a4"
            it.playerApi.play(playlistURI)
            Log.e("Baglandi", "deneme")
            // Subscribe to PlayerState
            it.playerApi.subscribeToPlayerState().setEventCallback {
                track = it.track
            }
        }

    }
}