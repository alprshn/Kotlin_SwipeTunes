package com.example.kotlin_spotify_random_like_app

import AddTracksRequest
import Album
import AlbumsResponse
import CreatePlaylistID
import PlayRequest
import PlayingTrack
import Playlist
import PlaylistRequest
import SpotifyTokenResponse
import SpotifyUser
import org.json.JSONObject
import retrofit2.Call
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
        ): AlbumsResponse // SearchResult, API'den dönen verilere uygun bir sınıf olmalıdır.

        @POST("users/{user_id}/playlists")
        suspend fun createPlaylist(
                @Path("user_id") userId: String,
                @Header("Authorization") token: String,
                @Body playlistRequest: PlaylistRequest
        ):CreatePlaylistID

        @GET("me")
        suspend fun userID(
                @Header("Authorization") token: String,
        ):SpotifyUser

        @POST("playlists/{playlist_id}/tracks")
        suspend fun addItemPlaylist(
                @Path("playlist_id") playlistId: String,
                @Header("Authorization") token: String,
                @Body addTracksRequest: AddTracksRequest
        )

        @GET ("me/player/currently-playing")
        suspend fun getCurrentPlaying(
                @Header("Authorization") token: String,
        ):PlayingTrack

        @GET("albums/{album_id}")
        suspend fun getAlbum(
                @Path("album_id") albumId: String,
                @Header("Authorization") authToken: String
        ):Album

        @POST("/api/token")
        @FormUrlEncoded
        suspend fun getToken(
                @Header("Authorization") token: String,
                @Field("code") code: String,
                @Field("client_id") clientId: String,
                @Field("redirect_uri") redirectUri: String,
                @Field("grant_type") grantType: String
        ): SpotifyTokenResponse
}

