package com.example.kotlin_spotify_random_like_app.model.data

data class Track(
    val album: Album,
    val artists: List<Artist>,
    val available_markets: List<String>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val popularity: Int,
    val preview_url: String?,
    val track_number: Int,
    val type: String,
    val uri: String,
    val is_local: Boolean
)

data class Tracks(
    val items: List<Track>,
    val total: Int,
    val limit: Int
)

data class TrackResponse(
    val tracks: Tracks
)
