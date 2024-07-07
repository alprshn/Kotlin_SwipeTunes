package com.example.kotlin_spotify_random_like_app.model.data

data class Album(
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val type: String,
    val uri: String,
    val artists: List<Artist>,
    val tracks: Tracks,
    val genres: List<String>,
    val label: String,
    val popularity: Int
)