package com.example.kotlin_spotify_random_like_app

import AddTracksRequest
import CreatePlaylistID
import Playlist
import PlaylistRequest
import RefreshTokenResponse
import SpotifyTokenResponse
import SpotifyUser
import TrackResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyService {




        @GET("search")
        suspend fun searchAlbum(
                @Header("Authorization") authorization: String,
                @Query("q") query: String,
                @Query("type") type: String = "track"
        ): TrackResponse // SearchResult, API'den dönen verilere uygun bir sınıf olmalıdır.

        @POST("users/{user_id}/playlists")
        suspend fun createPlaylist(
                @Path("user_id") userId: String,
                @Header("Authorization") token: String,
                @Body playlistRequest: PlaylistRequest
        ):CreatePlaylistID

        @GET("me")
        suspend fun getUserProfile(
                @Header("Authorization") token: String,
        ):SpotifyUser

        @POST("playlists/{playlist_id}/tracks")
        suspend fun addItemToPlaylist(
                @Path("playlist_id") playlistId: String,
                @Header("Authorization") token: String,
                @Body addTracksRequest: AddTracksRequest
        )


        @POST("/api/token")
        @Headers("Content-Type: application/x-www-form-urlencoded")
        @FormUrlEncoded
        suspend fun getToken(
                @Header("Authorization") token: String,
                @Field("code") code: String,
                @Field("client_id") clientId: String,
                @Field("redirect_uri") redirectUri: String,
                @Field("grant_type") grantType: String
        ): SpotifyTokenResponse


        @POST("api/token")
        @Headers("Content-Type: application/x-www-form-urlencoded")
        @FormUrlEncoded
        suspend fun refreshToken(
                @Header("Authorization") token: String,
                @Field("grant_type") grantType: String,
                @Field("refresh_token") refreshToken: String,
        ):RefreshTokenResponse
}

