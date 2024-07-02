package com.example.kotlin_spotify_random_like_app

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData

class NetworkManager(contex:Context): LiveData<Boolean>() {
    private var connectivityManager = contex.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


}