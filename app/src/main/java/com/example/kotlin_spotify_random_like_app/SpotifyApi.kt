package com.example.kotlin_spotify_random_like_app

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpotifyApi {
    private val BASE_URL = "https://api.spotify.com/v1/"

    fun create(): SpotifyService {
        val client = OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyService::class.java)
    }
}