package com.example.kotlin_spotify_random_like_app

import Playlist
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SpotifyService {
        @GET("playlists/{playlistId}")
        suspend fun getPlaylist(
                @Path("playlistId") playlistId: String,
                @Header("Authorization") authorization: String
        ): Playlist

        @PUT("me/player/pause")
        suspend fun pause(
                @Header("Authorization") authorization: String
        )
}
