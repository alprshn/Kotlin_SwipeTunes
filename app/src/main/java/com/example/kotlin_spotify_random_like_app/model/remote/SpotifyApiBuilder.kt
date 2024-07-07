package com.example.kotlin_spotify_random_like_app.model.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpotifyApiBuilder {
    private const val BASE_URL = "https://api.spotify.com/v1/"
    private const val ACCOUNTS_BASE_URL = "https://accounts.spotify.com/"


    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val accountsRetrofit = Retrofit.Builder()
        .baseUrl(ACCOUNTS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: SpotifyApiService by lazy { retrofit.create(SpotifyApiService::class.java) }

    val accountsService: SpotifyApiService by lazy { accountsRetrofit.create(SpotifyApiService::class.java) }

}