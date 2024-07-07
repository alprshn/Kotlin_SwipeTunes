package com.example.kotlin_spotify_random_like_app.model.data

data class SpotifyUser(
    val country: String,
    val display_name: String,
    val email: String,
    val explicit_content: ExplicitContent,
    val external_urls: ExternalUrls,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<Image>,
    val product: String,
    val type: String,
    val uri: String
)
data class ExplicitContent(
    val filter_enabled: Boolean,
    val filter_locked: Boolean
)
data class Followers(
    val href: Any?,
    val total: Int
)