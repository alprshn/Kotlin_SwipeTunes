package com.example.kotlin_spotify_random_like_app

class ItemModel(
    private var _image: Int,
    private val nama: String,
    private val usia: String,
    private val kota: String
) {
    constructor() : this(0, "", "", "")

    val image: Int
        get() = _image

    fun getNama(): String {
        return nama
    }

    fun getUsia(): String {
        return usia
    }

    fun getKota(): String {
        return kota
    }
}
