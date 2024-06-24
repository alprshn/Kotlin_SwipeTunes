package com.example.kotlin_spotify_random_like_app

import CreatePlaylistID
import Offset
import PlayRequest
import SpotifyTokenResponse
import TrackInfoList
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay


object  SpotifyApiManager {
    private lateinit var spotifyApi: SpotifyApi
    var accessToken:String =""
    var refreshToken: String? = null

    private val clientId = "1e6d0591bbb64af286b323ff7d26ce0f"
    private val clientSecret = "f22d019e70f345f5994d22d44f6b5dc2"
    private val redirectUri = "http://com.example.kotlin_spotify_random_like_app/callback"
    val scope = "streaming user-modify-playback-state user-read-private playlist-read playlist-read-private playlist-modify-private playlist-modify-public user-read-email user-read-recently-played user-read-currently-playing"
    val responseType = "code"
    //private lateinit var albumUri :String
    private var randomOffset :Int = 0
    var tokenCode: String? = null
    val trackList = mutableListOf<TrackInfoList>() // Track sınıfı şarkı bilgilerini tutar, getAlbum.tracks.items[0] gibi nesneleri temsil eder.
    val state = generateRandomString(16)

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
                val getAlbum = spotifyApi.service.getAlbum(albumID, "Bearer $accessToken")
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
                val response = spotifyApi.service.searchAlbum("q=$randomSeed", "Bearer $accessToken")
                if (response.albums.items.isNotEmpty()) {
                    val album = response.albums.items[randomAlbum]
                    val albumID = album.id
                    randomOffset = (Math.random() * (album.total_tracks - 1)).toInt()
                    infoAlbum(albumID, randomOffset)
                    Log.e("Trac ACCESS", accessToken.toString())
                } else {
                    Log.e("Error", "No tracks found.")
                }
            } catch (e: Exception) {
                Log.e("Error Track", "Error: ${e.message}")
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


    fun redirectToSpotifyLogin() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.e("token", tokenCode.toString())
                Log.e("Error", getAuthorizationHeader(clientId,clientSecret))
                Log.e("tokenCode Error", tokenCode.toString())

                val tokenResponse = spotifyApi.accountsService.getToken(getAuthorizationHeader(clientId,clientSecret),
                    tokenCode.toString(),clientId,
                    redirectUri,"authorization_code")

                accessToken = tokenResponse.access_token
                refreshToken = tokenResponse.refresh_token
               // getRefreshToken()


            } catch (e: Exception) {
                Log.e("Error", "Error Play Login: ${e.message}")
            }
        }
    }



    fun getAuthorizationHeader(clientId: String, clientSecret: String): String {
        val credentials = "$clientId:$clientSecret"
        return "Basic " + Base64.encodeToString(credentials.toByteArray(),Base64.NO_WRAP)

    }


    private fun generateRandomString(length: Int): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")

    }

    fun getRefreshToken() {

        CoroutineScope(Dispatchers.IO).launch {
            try {
               Log.e("Error Refresh Token", refreshToken.toString())
                updateRefreshToken(refreshToken)
                val refreshTokenResponse = spotifyApi.accountsService.refreshToken(getAuthorizationHeader(clientId,clientSecret),"refresh_token",
                    refreshToken.toString())
                accessToken = refreshTokenResponse.access_token
                updateRefreshToken(refreshTokenResponse.refresh_token)
                Log.e("tokenRef", refreshToken.toString())
                Log.e("AccessToken", accessToken.toString())


            } catch (e: Exception) {
                Log.e("Error", "Error Play: ${e.message}")
            }
        }

    }
    private fun updateRefreshToken(newToken: String?) {
        if (newToken == null) {
            Log.e("Error", "Attempted to update refreshToken with null.")
            return
        }
        refreshToken = newToken
    }
}
