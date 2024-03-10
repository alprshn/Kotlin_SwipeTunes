package com.example.kotlin_spotify_random_like_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackView

class SpotifySwipeMusic : AppCompatActivity() {
    private lateinit var manager:CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify_swipe_music)

        var cardStackView: CardStackView = findViewById(R.id.cardStackView)

    }
}