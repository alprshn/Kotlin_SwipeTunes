package com.example.kotlin_spotify_random_like_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_spotify_random_like_app.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val BASE_URL = "https://api.spotify.com/v1/"
    private lateinit var spotifyService: SpotifyService
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fun generateRandomString(length: Int): String {
            val possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            val random = SecureRandom()
            val values = ByteArray(length)
            random.nextBytes(values)
            return values.map { possible[it.toInt() and 0xff % possible.length] }.joinToString("")
        }

        val codeVerifier = generateRandomString(64)






        // Retrofit istemcisini oluştur
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val showLinearLayoutDemo = findViewById<Button>(R.id.button)

        // SpotifyService arayüzünü uygula
        spotifyService = retrofit.create(SpotifyService::class.java)

        // Sanatçı verilerini al
        getArtistData()
        binding.button.setOnClickListener{
            val intent = Intent(this, SpotifySwipeMusic::class.java)
            startActivity(intent)
        }
    }

    private fun getArtistData() {
        val accessToken = "BQCvRKlT8TXssyGA744PiTSd9PWg4Gb_1cNvqTYj45YCt6KYgUWkVD6Mx8CzJABxxpsJfd10YvDJpWPOB6mcpCmQLOiW79zjKIY6PJ_NJgvgaQFc6SM"
        val artistId = "4Z8W4fKeB5YxbusRsdQVPb" // Örnek bir sanatçı kimliği

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = spotifyService.getArtist("Bearer $accessToken", artistId)
                // Sanatçı verilerini kullanabilirsiniz, örneğin:
                val artistName = response.name
                val artistNa = response.images

                val followersCount = response.followers.total
                // Burada diğer verilere de erişebilirsiniz
                Log.e("denme","Artist Name: $artistName")
                Log.e("denme","Artist Name: $artistNa")
                Log.e("denme","Artist Name: $followersCount")

            } catch (e: Exception) {
                Log.e("denme","Error: ${e.message}")

            }
        }
    }

}