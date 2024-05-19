package com.example.kotlin_spotify_random_like_app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainActivityViewModel:ViewModel() {
    val accessToken: MutableLiveData<String?> = MutableLiveData()

    fun setAccessToken(token: String?) {
        accessToken.value = token
    }

    fun pause(token: String) {
        viewModelScope.launch {
            SpotifyApiManager.pause()
        }
    }

    fun next(token: String) {
        viewModelScope.launch {
            SpotifyApiManager.next()
        }
    }
}