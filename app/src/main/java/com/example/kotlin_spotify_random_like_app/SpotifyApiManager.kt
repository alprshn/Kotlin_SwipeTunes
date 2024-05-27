package com.example.kotlin_spotify_random_like_app

import CreatePlaylistID
import Offset
import PlayRequest
import TrackInfoList
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object  SpotifyApiManager {
    private lateinit var spotifyApi: SpotifyApi
    //private lateinit var albumUri :String
    private var randomOffset :Int = 0
    lateinit var accessToken:String
    val trackList = mutableListOf<TrackInfoList>() // Track sınıfı şarkı bilgilerini tutar, getAlbum.tracks.items[0] gibi nesneleri temsil eder.


    fun initialize(api: SpotifyApi) {
        spotifyApi = api
    }

    fun pause(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                spotifyApi.service.pause(accessToken)
            } catch (e: Exception) {
                Log.e("SpotifyApiManager", "Error: ${e.message}")
            }
        }

    }




    private fun infoAlbum(albumID:String, randomOffset:Int){
      //  val token = "Bearer ${MainActivity.accessToken}"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val getAlbum = spotifyApi.service.getAlbum(albumID, accessToken)
                val getTracks = getAlbum.tracks.items[randomOffset]
                //Log.e("Music Name",deneme.toString())
                // Log.e("Music Name", deneme.tracks.items.toString())
                Log.e(
                    "Music Name",
                    getAlbum.tracks.items[randomOffset].name
                )
                Log.e(
                    "Music ID",
                    getAlbum.tracks.items[randomOffset].id
                )
                trackList.add(TrackInfoList(getTracks.name, getAlbum.images[randomOffset].url, getAlbum.uri,randomOffset,"UnluADI" ))
               // Log.e("Music Name",trackList.toString())

                // Name, AlbumUri, Image, Description, ArtistName

            }
            catch (e: Exception) {
                Log.e("deneme", "Error: ${e.message}")
            }
        }


    }
    fun next() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                spotifyApi.service.next(accessToken)
            } catch (e: Exception) {
                Log.e("SpotifyApiManager", "Error: ${e.message}")
            }
        }
    }
    fun getNewTrackAndAddToList() {
        val randomSeed = generateQuery(2)
        val randomAlbum = (Math.random() * 19).toInt() // returns a random Integer from 0 to 20
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = spotifyApi.service.searchAlbum("q=$randomSeed", accessToken)
                if (response.albums.items.isNotEmpty()) {
                    val album = response.albums.items[randomAlbum]
                    val albumID = album.id
                    randomOffset = (Math.random() * (album.total_tracks - 1)).toInt()
                    infoAlbum(albumID, randomOffset)
                } else {
                    Log.e("Error", "No tracks found.")
                }
            } catch (e: Exception) {
                Log.e("Error", "Error: ${e.message}")
            }
        }
    }

    private fun generateQuery( length: Int): String{
        var result = ""
        var characters = "abcdefghijklmnopqrstuvwxyz"
        var charactersLength = characters.length
        for ( i in 0 until length){
            val randomIndex = (Math.random() * charactersLength).toInt()
            result += characters[Math.floor(Math.random() * charactersLength).toInt()]
        }
        return result.toString()
    }

    fun play( albumUri: String, offset: Int){
        Log.e("Random Offset", offset.toString())


        if (accessToken.isNullOrEmpty()) {
            Log.e("Error", "Access token is null or empty.")
            return
        }
        val requestBody = PlayRequest(albumUri, Offset(offset), 0)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.e("Play Request", requestBody.toString())
                Log.e("Play Acess", accessToken.toString())
                spotifyApi.service.play(requestBody, accessToken)
            } catch (e: Exception) {
                Log.e("Error", "Error Play: ${e.message}")
            }
        }
    }
    private fun updateTrackList(currentTrack: TrackInfoList?, nextTrack: TrackInfoList?) {
        trackList.clear()
        trackList.add(currentTrack!!)
        trackList.add(nextTrack!!)
    }




}