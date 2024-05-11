package com.example.kotlin_spotify_random_like_app

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface SpotifyService {

    interface SpotifyService {
        @Headers("Authorization: Bearer 1POdFZRZbvb...qqillRxMr2z")
        @POST("me/player/next")
        suspend fun nextTrack()
    }
}
