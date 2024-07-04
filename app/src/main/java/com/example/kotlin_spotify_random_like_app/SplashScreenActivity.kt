package com.example.kotlin_spotify_random_like_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_TIMER: Long = 3000
    private val splashName= "SplashPrefs"
    private val splashFirst= "FirstLogin"
    private lateinit var spotifyApi: SpotifyApi
    private lateinit var networkManager: NetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkFirstTimeLaunch()
    }

    private fun checkFirstTimeLaunch() {
        val sharedPref = getSharedPreferences(splashName, MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean(splashFirst, false)
        if (!isFirstTime) {
            startStartedActivity()
            finish()
            return
        }
        else{
            GlobalScope.launch(Dispatchers.IO) {
                // Arka planda yapılacak işlemler
                Thread.sleep(1000) // Örnek olarak 1 saniye bekleme simülasyonu
                spotifyApi = SpotifyApi
                SpotifyApiManager.initialize(spotifyApi)

                val sharedPrefAccessToken = getSharedPreferences("prefAccessToken", MODE_PRIVATE)
                val sharedPrefAccessTokens = sharedPrefAccessToken.getString("access_token",null)
                if (sharedPrefAccessTokens != null ){
                    SpotifyApiManager.accessToken = sharedPrefAccessTokens.toString()
                    Log.e("döngü","döngüde")
                }
                //Thread.sleep(1000) // Örnek olarak 1 saniye bekleme simülasyonu

                val sharedPrefToken = getSharedPreferences("prefToken", MODE_PRIVATE)
                val sharedPrefRefreshToken = sharedPrefToken.getString("refresh_token",null)
                if (sharedPrefRefreshToken != null ){
                    SpotifyApiManager.refreshToken = sharedPrefRefreshToken.toString()
                    Log.e("refrshdöngü","döngüde")
                }
                Thread.sleep(2000) // Örnek olarak 1 saniye bekleme simülasyonu

                SpotifyApiManager.getNewTrackAndAddToList(applicationContext)


                Log.e("denemeRefresh", sharedPrefRefreshToken.toString())
                Log.e("denemeAcessToken", SpotifyApiManager.accessToken.toString())

                Log.e("denemeACccessToken", sharedPrefAccessTokens.toString())
                Thread.sleep(1000) // Örnek olarak 1 saniye bekleme simülasyonu

                withContext(Dispatchers.Main) {
                    // Arayüzü güncelle veya kullanıcıya bildirim gönder
                    Toast.makeText(this@SplashScreenActivity, "İşlem tamamlandı!", Toast.LENGTH_SHORT).show()
                    startMainActivity()

                }
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun startStartedActivity() {
        val intent = Intent(this, StartedScreenActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun initNetworkDialog() {
        val dialog = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
            .setView(R.layout.custom_dialog)
            .setCancelable(false)
            .create()

        val networkManager = NetworkManager(this)
        networkManager.observe(this) {
            if (!it) {
                if (!dialog.isShowing) {
                    dialog.show()
                    CoroutineScope(Dispatchers.Main).launch {
                        waitForNetwork()
                    }
                }
            } else {
                if (dialog.isShowing) {
                    dialog.dismiss()  // 'hide' yerine 'dismiss' kullanmak genellikle daha doğru bir yaklaşımdır.
                    launchFirstTimeCheck()

                }
            }
        }
    }

    private suspend fun waitForNetwork() {
        while (!networkManager.value!!) {
            delay(1000)
        }
        launchFirstTimeCheck()
    }

    private fun launchFirstTimeCheck() {
        if (networkManager.value == true) {
            checkFirstTimeLaunch()
        }
    }
}