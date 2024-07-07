package com.example.kotlin_spotify_random_like_app.model.data

data class Image(
    val url: String,
    val height: Any?, // Height ve width nullable olabilir
    val width: Any?
)