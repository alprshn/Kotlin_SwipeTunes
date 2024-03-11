package com.example.kotlin_spotify_random_like_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_spotify_random_like_app.databinding.ActivityMainBinding
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val clientId = "1e6d0591bbb64af286b323ff7d26ce0f"
    private val redirectUri = "https://alprshn.github.io/"

    private val REQUEST_CODE  = 1337

    private var spotifyAppRemote: SpotifyAppRemote? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.e("Baglandi", "burada")
        binding.button.setOnClickListener{
            var builder: AuthorizationRequest.Builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri);
            builder.setScopes(arrayOf("streaming"))
            val request = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
            val intent = Intent(this, SpotifySwipeMusic::class.java)
            startActivity(intent)
        }
    }
    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
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
                val intentTrack = Intent(this, SpotifySwipeMusic::class.java) // Track List verilerini gönderme
                intentTrack.putExtra("name",track.name.toString() )
                val imageId = track.imageUri.toString() // Resim ekleme kısmı
                val imageUrl = ("https://i.scdn.co/image/${imageId.substringAfterLast(":").removeSuffix("'}")}")
                intentTrack.putExtra("imageUri",imageUrl)
                intentTrack.putExtra("artistName",track.artist.name.toString() )

                startActivity(intentTrack)

                Log.e("Baglandi", imageUrl + " by " + track.artist.name)
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {}
                AuthorizationResponse.Type.ERROR -> {}
                else -> {}
            }
        }
    }


    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }

    }

}