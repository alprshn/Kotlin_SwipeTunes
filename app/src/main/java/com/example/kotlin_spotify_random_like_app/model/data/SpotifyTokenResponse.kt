package com.example.kotlin_spotify_random_like_app.model.data

data class SpotifyTokenResponse(
    val access_token: String,
    val token_type: String,
    val scope: String,
    val expires_in: Int,
    val refresh_token: String
)