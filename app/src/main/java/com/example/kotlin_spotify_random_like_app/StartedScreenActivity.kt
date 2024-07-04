package com.example.kotlin_spotify_random_like_app

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import java.text.FieldPosition

class StartedScreenActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: Array<TextView>
    private lateinit var spotifyAuth: SpotifyConnection
    private lateinit var spotifyApi: SpotifyApi
    private lateinit var networkManager: NetworkManager

    private val prefsName = "AppPrefs"
    private val splashName= "SplashPrefs"
    private val splashFirst= "FirstLogin"
    private val firstTimeKey = "FirstTime"
    private val tokenKey = "SpotifyToken"  // Spotify token'i saklamak için anahtar
    private val clientId = "1e6d0591bbb64af286b323ff7d26ce0f"
    private val redirectUri = "http://com.example.kotlin_spotify_random_like_app/callback"
    private val REQUEST_CODE = 1337
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_started_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        networkManager = NetworkManager(this)

        setupSpotifyConnection()
        checkIsSpotifyInstalled()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        viewPager = findViewById(R.id.slider)
        dotsLayout = findViewById(R.id.dots)
        viewPager.adapter = SliderAdapter(this)
        viewPager.addOnPageChangeListener(changeListener)
        //checkConnection()
        addDots(0)
        checkFirstTimeLaunch()
        checkAutoLogin()  // Otomatik giriş kontrolü
    }
    private fun setupSpotifyConnection() {
        spotifyAuth = SpotifyConnection(this)
        spotifyApi = SpotifyApi
        SpotifyApiManager.initialize(spotifyApi)

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.CODE, redirectUri)
            builder.setScopes(arrayOf("streaming", "user-modify-playback-state", "user-read-private", "playlist-read", "playlist-read-private", "playlist-modify-private", "playlist-modify-public", "user-read-email", "user-read-recently-played", "user-read-currently-playing"))
            val request = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
        }
    }

    private fun checkIsSpotifyInstalled(){
        val spotifyInstalledLiveData = SpotifyInstalledLiveData(this)

        spotifyInstalledLiveData.observe(this, Observer { isInstalled ->
            if (isInstalled) {
                // Spotify uygulaması yüklü
                Log.e("Spotify uygulaması yüklü.","Spotify uygulaması yüklü")
            } else {
                // Spotify uygulaması yüklü değil
                Log.e("Spotify uygulaması yüklü değil.","Spotify uygulaması yüklü değil")
                //println("Spotify uygulaması yüklü değil.")
            }
        })

    }
    private fun checkFirstTimeLaunch() {
        val sharedPref = getSharedPreferences(prefsName, MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean(firstTimeKey, true)
        if (!isFirstTime) {
            startMainActivity()
            finish()
            return
        }
    }




    private fun checkAutoLogin() {
        val sharedPref = getSharedPreferences(prefsName, MODE_PRIVATE)
        val token = sharedPref.getString(tokenKey, null)
        if (token != null) {
            SpotifyApiManager.accessToken = "Bearer $token"
            startMainActivity()
        }
    }
    private fun addDots(position: Int){
        dots = Array(1) { TextView(this) }
        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i].text = Html.fromHtml("&#8226;")
            dots[i].textSize = 35f // Kullanılabilir bir textSize ayarı

            dotsLayout.addView(dots[i])
        }

        if (dots.size>0){
            dots[position].setTextColor(resources.getColor(com.google.android.material.R.color.design_default_color_primary_dark))
        }
    }

    val changeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {
            addDots(position)
        }

        override fun onPageScrollStateChanged(state: Int) {

        }

    }


    override fun onStart() {
        super.onStart()
        if (networkManager.value == true) {
            spotifyAuth?.connectionStart()
        } else {
            initNetworkDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.CODE -> {
                    SpotifyApiManager.tokenCode = response.code
                    SpotifyApiManager.redirectToSpotifyLogin()

                    Log.e("denemetoken","MERHABA")
                    Log.e("denemetoken",response.code.toString())
                    val splashSharedPref = getSharedPreferences(splashName, MODE_PRIVATE)
                    splashSharedPref.edit().putBoolean(splashFirst, true).apply()
                    val sharedPref = getSharedPreferences(prefsName, MODE_PRIVATE)
                    sharedPref.edit().putBoolean(firstTimeKey, false).apply()
                    startSplashActivity()
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("SpotifyAuthError", "Authentication error: ${response.error}")
                }
                else -> {
                    // Handle other cases
                }
            }
        }
    }
    private fun startSplashActivity() {
        val intent = Intent(this, SplashScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
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
                }
            } else {
                if (dialog.isShowing) {
                    spotifyAuth?.connectionStart()
                    dialog.dismiss()
// 'hide' yerine 'dismiss' kullanmak genellikle daha doğru bir yaklaşımdır.
                }
            }
        }
    }

}