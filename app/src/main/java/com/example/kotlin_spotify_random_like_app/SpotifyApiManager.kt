package com.example.kotlin_spotify_random_like_app

import AddTracksRequest
import CreatePlaylistID
import Offset
import PlayRequest
import SpotifyTokenResponse
import TrackInfoList
import android.content.Context
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



    fun addItemPlaylist(playlistID: String, trackUri: String, position: Int = 0){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val addTracksRequest = AddTracksRequest(uris = listOf(trackUri), position = position)
                spotifyApi.service.addItemToPlaylist(playlistID,"Bearer $accessToken",addTracksRequest)
            } catch (e: Exception) {
                Log.e("SpotifyApiManager", "Error: ${e.message}")
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
    fun getNewTrackAndAddToList(context: Context) {
        val randomSeed = generateQuery(2)
        Log.e("Random Seed", randomSeed.toString())
        var retry = true
            CoroutineScope(Dispatchers.IO).launch {
                while (retry) {
                    try {
                        Log.e("AcessToken Track", accessToken.toString())
                        val response =
                            spotifyApi.service.searchAlbum("Bearer $accessToken", randomSeed)
                        val randomTrackNumber = (Math.random() * response.tracks.limit-1).toInt() // returns a random Integer from 0 to 20
                        Log.e("randomTrackNumber Track", randomTrackNumber.toString())
                        if (response.tracks.items.isNotEmpty()) {
                            val track = response.tracks.items[randomTrackNumber]// İlk track'i alıyoruz
                            val album = track.album
                            //val artist = response.tracks.items[randomTrackNumber].artists[0]
                            var artists = track.artists.joinToString(", ") { it.name }

                            Log.e("Trac ACCESS", accessToken.toString())
                            //Log.e("Trac total tracks", album.total_tracks.toString())
                            Log.e("aLBUM NAME", album.name)
                            Log.e("Track NAME", track.name)
                            trackList.add(
                                TrackInfoList(
                                    track.name,
                                    album.images[0].url,
                                    track.uri,
                                    artists
                                )
                            )
                        } else {
                            Log.e("Error", "No tracks found.")
                        }
                        retry = false
                    } catch (e: Exception) {
                        Log.e("Error Track", "Error: ${e.message}")
                        getRefreshToken()
                        delay(1000)
                        saveRefreshToken(context)
                        saveAccessToken(context)
                        retry = true
                    }
                }
            }
    }
    private fun saveRefreshToken(context: Context){ //Burada sharedPreferences'a refreshToken'i ekledik
        val sharedPreferences:SharedPreferences = context.getSharedPreferences("prefToken",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("refresh_token",refreshToken).apply()
    }

    private fun saveAccessToken(context: Context){ //Burada sharedPreferences'a refreshToken'i ekledik
        val sharedPreferences:SharedPreferences = context.getSharedPreferences("prefAccessToken",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("access_token",accessToken).apply()
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
