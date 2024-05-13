package com.example.kotlin_spotify_random_like_app

import PlayRequest
import Playlist
import TrackResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

        @POST("me/player/next")
        suspend fun next(
                @Header("Authorization") authorization: String
        )

        @PUT("me/player/play")
        @Headers("Content-Type: application/json")
        suspend fun play(
                @Body body: PlayRequest,
                @Header("Authorization") authorization: String
        )

        @GET("search?type=album")
        suspend fun searchAlbum(
                @Query("q") query: String,
                @Header("Authorization") authorization: String
        ): TrackResponse // SearchResult, API'den dönen verilere uygun bir sınıf olmalıdır.
}
