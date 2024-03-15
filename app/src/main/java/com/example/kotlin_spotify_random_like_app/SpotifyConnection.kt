package com.example.kotlin_spotify_random_like_app

import android.content.Context
import android.content.Intent
import android.util.Log
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.protocol.types.Track
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
class SpotifyConnection {
     val clientId = "1e6d0591bbb64af286b323ff7d26ce0f"
     val redirectUri = "https://alprshn.github.io/"

     val REQUEST_CODE  = 1337

     var spotifyAppRemote: SpotifyAppRemote? = null
    fun ConnectionStart(context: Context){
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                // Now you can start interacting with App Remote
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("Baglandi", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }

    private fun connected() {
        spotifyAppRemote?.let {
            // Play a playlist
            val playlistURI = "spotify:playlist:3GhEaVPCWa7ZVTyis5WV54?si=79a14006b45f49a4"
            it.playerApi.play(playlistURI)
            Log.e("Baglandi", "deneme")
            // Subscribe to PlayerState
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track

                Log.e("Baglandi", track.name + " by " + track.artist.name)
            }
        }

    }

}