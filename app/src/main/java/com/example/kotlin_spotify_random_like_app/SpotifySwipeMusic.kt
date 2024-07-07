package com.example.kotlin_spotify_random_like_app

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.example.kotlin_spotify_random_like_app.SpotifyApiManager.accessToken
import com.example.kotlin_spotify_random_like_app.SpotifyApiManager.trackList
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeableMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SpotifySwipeMusic : AppCompatActivity() {
    private lateinit var manager:CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter
    private lateinit var spotifyApi: SpotifyApi
    private lateinit var spotifyConnection: SpotifyConnection

    private var count:Int = 0
    companion object {
        private const val TAG = "SpotifySwipeMusic"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify_swipe_music)
        initNetworkDialog()
        initializeSpotify()
        initializeUI()
        checkTrackListError()
    }
    private fun initializeSpotify() {
        spotifyApi = SpotifyApi
        val createPlayList = CreatePlayList(this, spotifyApi, accessToken)
        createPlayList.create()
        spotifyConnection = SpotifyConnection(this).apply {
            onConnected = { playTrackIfAvailable() }

            onConnectionFailed = { error ->
                Toast.makeText(this@SpotifySwipeMusic, "Connection failed: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
        spotifyConnection.connectionStart()
    }

    private fun playTrackIfAvailable() {
        if (trackList.isNotEmpty()) {
            spotifyConnection.play(trackList[count].trackUri)
        }
    }
    override fun onPause() {
        super.onPause()
        spotifyConnection.pause()
        spotifyConnection.stopCheckingPlayerState()

    }

    override fun onResume() {
        super.onResume()
        spotifyConnection.resume()
        spotifyConnection.startCheckingPlayerState()
    }

    override fun onStop() {
        super.onStop()
        spotifyConnection.pause()
        spotifyConnection.stopCheckingPlayerState()

    }

    override fun onDestroy() {
        super.onDestroy()
        spotifyConnection.pause()
        spotifyConnection.stopCheckingPlayerState()
        spotifyConnection.disconnect()

    }
    private fun checkTrackListError() {
        CoroutineScope(Dispatchers.IO).launch {
            while (trackList.isEmpty() || trackList[count].trackUri.isEmpty()) {
                SpotifyApiManager.getRefreshToken()
                delay(2000)
                saveTokens()
                delay(2000)
                Toast.makeText(
                    this@SpotifySwipeMusic,
                    "Track listesi yüklenemedi.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            withContext(Dispatchers.Main) {
                Log.e(TAG, "Track listesi boş değil.")
                SpotifyApiManager.getNewTrackAndAddToList(applicationContext)
                loadDataAndSetupCards()
            }
        }
    }
    private fun saveTokens() {
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("prefToken", Context.MODE_PRIVATE)
        val refreshTokenEditor: SharedPreferences.Editor = sharedPreferences.edit()
        refreshTokenEditor.putString("refresh_token", SpotifyApiManager.refreshToken).apply()

        val accessSharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("prefAccessToken", Context.MODE_PRIVATE)
        val accessTokenEditor: SharedPreferences.Editor = accessSharedPreferences.edit()
        accessTokenEditor.putString("access_token", SpotifyApiManager.refreshToken).apply()
    }
    private fun setupBackgroundAnimation() {
        val constraintLayout: ConstraintLayout = findViewById(R.id.swipeLayout)
        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()
    }

    private fun initializeUI() {
        setupBackgroundAnimation()
        setupCardStackView()
    }

    private fun setupCardStackView() {
        val cardStackView: CardStackView = findViewById(R.id.cardStackView)
        manager = CardStackLayoutManager(this, object:CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                Log.d(TAG, "onCardDragging: d=${direction?.name} ratio=$ratio")
            }

            override fun onCardSwiped(direction: Direction?) {
                Log.d(TAG, "onCardSwiped: p=${manager.topPosition} d=$direction")
                when (direction) {
                    Direction.Right -> {
                        addTrackToPlaylist()
                        playNextTrack()
                        loadDataAndSetupCards()
                        removeOldTrack()
                        Toast.makeText(this@SpotifySwipeMusic, "Direction Right", Toast.LENGTH_SHORT).show()
                    }
                    Direction.Left -> {
                        SpotifyApiManager.getNewTrackAndAddToList(applicationContext)
                        playNextTrack()
                        loadDataAndSetupCards()
                        removeOldTrack()
                        Toast.makeText(this@SpotifySwipeMusic, "Direction Left", Toast.LENGTH_SHORT).show()
                    }
                    null -> TODO()
                    Direction.Top -> TODO()
                    Direction.Bottom -> TODO()
                }

                if (manager.topPosition == adapter.itemCount - 5) {
                    paginate()
                }
                Thread.sleep(375)
            }
            override fun onCardRewound() {
                Log.d(TAG, "onCardRewound: ${manager.topPosition}")
            }

            override fun onCardCanceled() {
                Log.d(TAG, "onCardCanceled: ${manager.topPosition}")
            }

            override fun onCardAppeared(view: View?, position: Int) {
                val tv = view?.findViewById<TextView>(R.id.item_name)
                Log.d(TAG, "onCardAppeared: $position, nama: ${tv?.text}")            }

            override fun onCardDisappeared(view: View?, position: Int) {
                val tv = view?.findViewById<TextView>(R.id.item_name)
                Log.d(TAG, "onCardDisappeared: $position, nama: ${tv?.text}")            }

        })
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        adapter = CardStackAdapter(addList())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator()
    }

    private fun addTrackToPlaylist() {
        pullPlaylistID(trackList[count].trackUri)
        SpotifyApiManager.getNewTrackAndAddToList(applicationContext)
        spotifyConnection.queue(trackList[count].trackUri)
    }
    private fun playNextTrack() {
        if (count < trackList.size - 1) {
            count++
        }
        spotifyConnection.play(trackList[count].trackUri)
    }
    private fun removeOldTrack() {
        if (trackList.isNotEmpty() && count > 0) {
            trackList.removeAt(0)
            count--
        }
    }
    private fun pullPlaylistID(trackUri:String){
        val sharedPreferences = getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val playListID = sharedPreferences.getString("playlist_id", null)
        if (playListID != null) {
            SpotifyApiManager.addItemPlaylist(playListID, trackUri)
        } else {
            Log.e(TAG, "Playlist ID is null.")
        }
    }
    private fun loadDataAndSetupCards() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                // Veri yükleme tamamlandığında, kartları kurun
                if (trackList.isNotEmpty()) {
                    val cards = addList() // Kartları oluştur
                    adapter.setItems(cards) // Adapter'a kartları set edin
                    adapter.notifyDataSetChanged() // Adapter'a güncelleme olduğunu bildir
                } else {
                    Log.e(TAG, "Veri yüklenemedi veya track listesi boş.")
                }
            }
        }
    }

    private fun addList(): List<ItemModel> {
        val itemsList = ArrayList<ItemModel>()

        // Listenin boş olup olmadığını kontrol et
        if (trackList.isNotEmpty()) {
            val currentTrack = trackList[count]
            itemsList.add(ItemModel(currentTrack.imageUri, currentTrack.trackName, currentTrack.artistName, ""))
        } else {
            // Liste boşsa, bir hata mesajı göster veya uygun bir işlem yap
            Log.e(TAG, "Track listesi boş.")
            // Burada gerekiyorsa veri yükleme işlemini tekrar başlatabilirsiniz veya kullanıcıya bilgi verebilirsiniz.
        }

        return itemsList
    }

    private fun paginate(){
        val old: List<ItemModel> = adapter.getItems()
        val new  = ArrayList(addList())
        val callback = CardStackCallback(old, new )
        val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)
        adapter.setItems(new)
        result.dispatchUpdatesTo(adapter)
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
                    dialog.dismiss()  // 'hide' yerine 'dismiss' kullanmak genellikle daha doğru bir yaklaşımdır.
                }
            }
        }
    }
}