package com.example.kotlin_spotify_random_like_app

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData

class NetworkManager(contex:Context): LiveData<Boolean>() {
    private var connectivityManager = contex.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            postValue(false)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }

    }
}