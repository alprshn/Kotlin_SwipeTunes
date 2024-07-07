package com.example.kotlin_spotify_random_like_app

import android.content.Context
import androidx.lifecycle.LiveData
import com.spotify.android.appremote.api.SpotifyAppRemote

class SpotifyInstallationStatusLiveData(private val context: Context) : LiveData<Boolean>() {

    override fun onActive() {
        super.onActive()
        checkSpotifyInstalled()
    }

    private fun checkSpotifyInstalled() {
        val isInstalled = SpotifyAppRemote.isSpotifyInstalled(context)
        postValue(isInstalled)
    }
}