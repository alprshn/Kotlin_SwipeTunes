package com.example.kotlin_spotify_random_like_app

import AddTracksRequest
import Offset
import PlayRequest
import PlaylistRequest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_spotify_random_like_app.databinding.ActivityMainBinding
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
   // private lateinit var spotifyService: SpotifyService
    // Initialize SpotifyApiManager


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        animateBackground()
        val sharedPreferences: SharedPreferences = getSharedPreferences("tokenShared", MODE_PRIVATE)
        //val storedAccessToken = sharedPreferences.getString("access_token", null)
        //val storedRefreshToken = sharedPreferences.getString("refresh_token", null)
        //Log.e("tokenlarAccess",storedAccessToken.toString())
        //Log.e("tokenlarRefresh",storedRefreshToken.toString())

        //SpotifyApiManager.getRefreshToken()
        //Log.e("TkenControl", SpotifyApiManager.accessToken)
        //Log.e("RefresTkenControl", SpotifyApiManager.refreshToken)
        //SpotifyApiManager.getNewTrackAndAddToList()
        GlobalScope.launch(Dispatchers.IO) {
            // Arka planda yapılacak işlemler
            Thread.sleep(2000) // Örnek olarak 1 saniye bekleme simülasyonu
            SpotifyApiManager.getRefreshToken()
            SpotifyApiManager.getNewTrackAndAddToList()
            val sharedPrefToken = getSharedPreferences("prefToken", MODE_PRIVATE)
            val sharedPrefRefreshToken = sharedPrefToken.getString("refresh_token","merhaba")
            Log.e("denemeRefresh", sharedPrefRefreshToken.toString())

            withContext(Dispatchers.Main) {
                // Arayüzü güncelle veya kullanıcıya bildirim gönder
                Toast.makeText(this@MainActivity, "İşlem tamamlandı!", Toast.LENGTH_SHORT).show()
            }
        }



        setupButtons()


    }
    private fun setupButtons() {
        binding.button4.setOnClickListener {
            // Open Spotify swipe music activity
            val intent = Intent(this, SpotifySwipeMusic::class.java)
            startActivity(intent)
        }

        binding.button2.setOnClickListener {
           // SpotifyApiManager.redirectToSpotifyLogin()
            SpotifyApiManager.getNewTrackAndAddToList()

        }
        binding.button3.setOnClickListener {
            SpotifyApiManager.getRefreshToken()

        }

        binding.button5.setOnClickListener {
        }
        // Setup other buttons similarly
    }

    private fun saveRefreshToken(){ //Burada sharedPreferences'a refreshToken'i ekledik
        val sharedPreferences:SharedPreferences = getSharedPreferences("tokens", MODE_PRIVATE)
        val refreshToken: SharedPreferences.Editor = sharedPreferences.edit()
        refreshToken.putString("refreshToken",SpotifyApiManager.refreshToken).apply()
    }


    private fun animateBackground() {
        val constraintLayout:ConstraintLayout = binding.mainLayout
        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }








}