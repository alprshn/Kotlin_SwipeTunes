package com.example.kotlin_spotify_random_like_app

import AddTracksRequest
import Offset
import PlayRequest
import PlaylistRequest
import android.app.Activity
import android.app.AlertDialog
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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.kotlin_spotify_random_like_app.databinding.ActivityMainBinding
import com.google.android.material.R.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ramotion.circlemenu.CircleMenuView
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
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
   // private lateinit var spotifyService: SpotifyService
    // Initialize SpotifyApiManager
    private lateinit var dialog:AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dialog = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
            .setView(R.layout.custom_dialog)
            .setCancelable(false)
            .create()
        animateBackground()
        //val sharedPreferences: SharedPreferences = getSharedPreferences("tokenShared", MODE_PRIVATE)
        //val storedAccessToken = sharedPreferences.getString("access_token", null)
        //val storedRefreshToken = sharedPreferences.getString("refresh_token", null)
        //Log.e("tokenlarAccess",storedAccessToken.toString())
        //Log.e("tokenlarRefresh",storedRefreshToken.toString())

        //SpotifyApiManager.getRefreshToken()
        //Log.e("TkenControl", SpotifyApiManager.accessToken)
        //Log.e("RefresTkenControl", SpotifyApiManager.refreshToken)
        //SpotifyApiManager.getNewTrackAndAddToList()


        saveRefreshToken()

        saveAccessToken()
        setupButtons()
        Log.e("denemeRefreshToken", SpotifyApiManager.refreshToken.toString())
        CircularMenu()

    }
    private fun setupButtons() {
        binding.button4.setOnClickListener {
            // Open Spotify swipe music activity
            val intent = Intent(this, SpotifySwipeMusic::class.java)
            startActivity(intent)
        }

       // binding.button2.setOnClickListener {
           // SpotifyApiManager.redirectToSpotifyLogin()
          //  SpotifyApiManager.getNewTrackAndAddToList()

        //}
        //binding.button3.setOnClickListener {
          //  SpotifyApiManager.getRefreshToken()

       // }

        //binding.button5.setOnClickListener {
           // val request = PeriodicWorkRequestBuilder<UploadWorker>(15,TimeUnit.MINUTES)
               // .setInitialDelay(5,TimeUnit.SECONDS)
                //.build()
            //WorkManager.getInstance(this).enqueue(request)

           // WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id).observe(this) {
                //Log.e("Status",it.state.name)
           // }
       // }
        // Setup other buttons similarly
    }

    private fun saveRefreshToken(){ //Burada sharedPreferences'a refreshToken'i ekledik
        val sharedPreferences:SharedPreferences = getSharedPreferences("prefToken", MODE_PRIVATE)
        val refreshToken: SharedPreferences.Editor = sharedPreferences.edit()
        refreshToken.putString("refresh_token",SpotifyApiManager.refreshToken).apply()
    }

    private fun saveAccessToken(){ //Burada sharedPreferences'a refreshToken'i ekledik
        val sharedPreferences:SharedPreferences = getSharedPreferences("prefAccessToken", MODE_PRIVATE)
        val refreshToken: SharedPreferences.Editor = sharedPreferences.edit()
        refreshToken.putString("access_token",SpotifyApiManager.accessToken).apply()
    }

    private fun animateBackground() {
        val constraintLayout:ConstraintLayout = binding.mainLayout
        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(1000)
        animationDrawable.setExitFadeDuration(2000)
        animationDrawable.start()
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }


    private fun CircularMenu(){
        val circleMenu = binding.circleMenu

        circleMenu.setEventListener(object : CircleMenuView.EventListener() {
            override fun onMenuOpenAnimationStart(view: CircleMenuView) {

            }
            override fun onButtonClickAnimationStart(view: CircleMenuView, buttonIndex: Int) {
                super.onButtonClickAnimationStart(view, buttonIndex)
                when (buttonIndex) {
                    0 -> {
                        Toast.makeText(this@MainActivity, "Home Button Clicked", Toast.LENGTH_SHORT).show()
                        // Diğer işlemler burada
                    }
                    1 -> {
                        Toast.makeText(this@MainActivity, "pLAVE Button Clicked", Toast.LENGTH_SHORT).show()
                        // Diğer işlemler burada
                    }
                }
            }
        })


    }






}