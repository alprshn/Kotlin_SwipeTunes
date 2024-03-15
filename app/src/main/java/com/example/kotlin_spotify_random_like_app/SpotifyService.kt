package com.example.kotlin_spotify_random_like_app

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SpotifyService {

    @GET("artists/{artistId}")
    suspend fun getArtist(
        @Header("Authorization") authorization: String,
        @Path("artistId") artistId: String
    ): ArtistResponse
}
