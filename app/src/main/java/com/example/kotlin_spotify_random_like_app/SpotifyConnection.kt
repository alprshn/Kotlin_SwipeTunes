package com.example.kotlin_spotify_random_like_app

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.SpotifyDisconnectedException
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.Direction


class SpotifyConnection(private val context: Context) {
     private val clientId = "1e6d0591bbb64af286b323ff7d26ce0f"
     private val redirectUri = "http://com.example.kotlin_spotify_random_like_app/callback"

     var spotifyAppRemote: SpotifyAppRemote? = null
    var onConnected: (() -> Unit)? = null
    var onConnectionFailed: ((Throwable) -> Unit)? = null
    private val handler = Handler(Looper.getMainLooper())
    private var checkPlayerStateRunnable: Runnable? = null

    fun connectionStart(){
        Log.e("MainActivity", "Oldu bu iis")

        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.e("SpotifyConnection", "Connected to Spotify.")
                onConnected?.invoke() // Bağlantı başarılı callback'i çağır
                startCheckingPlayerState() // Bağlantıdan sonra oyuncu durumunu kontrol etmeye başla
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("SpotifyConnection", "Failed to connect: ${throwable.message}", throwable)
                onConnectionFailed?.invoke(throwable)

            }
        })
    }
    fun play(trackUri: String) {
        spotifyAppRemote?.let {
            it.playerApi.play(trackUri).setErrorCallback { error ->
                Log.e("SpotifyConnection", "Error playing track: $trackUri, error: ${error.message}")
            }
            Log.e("SpotifyConnection", "Playing track: $trackUri")
        } ?: run {
            Log.e("SpotifyConnection", "Spotify App Remote is not connected yet.")
        }
    }


    fun pause() {
        spotifyAppRemote?.let {
            it.playerApi.pause().setErrorCallback { error ->
                Log.e("SpotifyConnection", "Error playing track:, error: ${error.message}")
            }
        } ?: run {
            Log.e("SpotifyConnection", "Spotify App Remote is not connected yet.")
        }
    }

    fun resume() {
        spotifyAppRemote?.let {
            it.playerApi.resume().setErrorCallback { error ->
                Log.e("SpotifyConnection", "Error playing track:, error: ${error.message}")
            }
        } ?: run {
            Log.e("SpotifyConnection", "Spotify App Remote is not connected yet.")
        }
    }

    fun queue(trackUri: String) {
        spotifyAppRemote?.let {
            it.playerApi.queue(trackUri).setErrorCallback { error ->
                Log.e("SpotifyConnection", "Error playing track:, error: ${error.message}")
            }
        } ?: run {
            Log.e("SpotifyConnection", "Spotify App Remote is not connected yet.")
        }
    }


    fun checkSpotifyInstalled(): Boolean {
        return SpotifyAppRemote.isSpotifyInstalled(context)
    }

    fun disconnect() {
         SpotifyAppRemote.disconnect(spotifyAppRemote)
    }


    fun startCheckingPlayerState() {
        checkPlayerStateRunnable = Runnable {
            spotifyAppRemote?.let {
                it.playerApi.playerState
                    .setResultCallback { playerState ->
                        val track = playerState.track
                        val currentPosition = playerState.playbackPosition
                        val duration = track.duration


                        Log.d("SpotifyConnection", "Now playing: ${track.name} by ${track.artist.name}")
                        Log.d("SpotifyConnection", "Current position: ${currentPosition}ms / ${duration}ms")


                        if (currentPosition >= duration - 1000 ) {
                            onTrackEnded()
                        }
                    }
                    .setErrorCallback { throwable ->
                        Log.e("SpotifyConnection", "Failed to get player state", throwable)
                        if (throwable is SpotifyDisconnectedException) {
                            connectionStart() // Bağlantı kesildiyse yeniden bağlanmayı dene
                        }
                    }
            }
            handler.postDelayed(checkPlayerStateRunnable!!, 1000) // Her saniye kontrol et
        }
        handler.post(checkPlayerStateRunnable!!)
    }

    private fun onTrackEnded() {
        pause()
    }
    fun stopCheckingPlayerState() {
        checkPlayerStateRunnable?.let { handler.removeCallbacks(it) }
    }
}