package com.example.kotlin_spotify_random_like_app.model.data

data class PlaylistRequest(
    val name: String,
    val description: String,
    val public: Boolean
)


data class AddTracksRequest(
    val uris: List<String>,
    val position: Int
)

data class CreatePlaylistID(
    val id: String
)
