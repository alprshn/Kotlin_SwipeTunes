package com.example.kotlin_spotify_random_like_app

import TrackInfoList
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
import com.example.kotlin_spotify_random_like_app.SpotifyApiManager.trackList
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeableMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SpotifySwipeMusic : AppCompatActivity() {
    private lateinit var manager:CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter
    private lateinit var spotifyApi: SpotifyApi
    private var count:Int = 0
    companion object {
        private const val TAG = "SpotifySwipeMusic"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify_swipe_music)

        var constraintLayout: ConstraintLayout = findViewById(R.id.swipeLayout)
        var animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()

        SpotifyApiManager.play(trackList[count].albumUri,trackList[count].offset)
        initializeUI()
        SpotifyApiManager.getNewTrackAndAddToList() // Veriyi asenkron olarak yükle
        loadDataAndSetupCards()

    }

    private fun initializeUI() {
        val cardStackView: CardStackView = findViewById(R.id.cardStackView)
        manager = CardStackLayoutManager(this, object:CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                Log.d(TAG, "onCardDragging: d=${direction?.name} ratio=$ratio")
            }

            override fun onCardSwiped(direction: Direction?) {
                Log.d(TAG, "onCardSwiped: p=${manager.topPosition} d=$direction")
                if (direction == Direction.Right) {
                    //SpotifyApiManager.play(trackList[count].albumUri,trackList[count].offset)
                    SpotifyApiManager.getNewTrackAndAddToList()
                    if (count < trackList.size - 1) {
                        count++
                    }

                    SpotifyApiManager.play(trackList[count].albumUri,trackList[count].offset)
                    loadDataAndSetupCards()
                    Toast.makeText(this@SpotifySwipeMusic, "Direction Right", Toast.LENGTH_SHORT).show()
                    if (trackList.size > 0 && count > 0) {  // Liste yeterince büyükse ve en az bir kaydırma yapılmışsa
                        trackList.removeAt(0)  // Liste başından eleman sil
                        count--  // Silme işlemi sonrası, count değerini güncelle
                    }
                } else if (direction == Direction.Top) {
                    Toast.makeText(this@SpotifySwipeMusic, "Direction Top", Toast.LENGTH_SHORT).show()
                } else if (direction == Direction.Left) {
                    SpotifyApiManager.getNewTrackAndAddToList()
                    if (count < trackList.size - 1) {
                        count++
                    }

                    SpotifyApiManager.play(trackList[count].albumUri,trackList[count].offset)
                    loadDataAndSetupCards()
                    Toast.makeText(this@SpotifySwipeMusic, "Direction Right", Toast.LENGTH_SHORT).show()
                    if (trackList.size > 0 && count > 0) {  // Liste yeterince büyükse ve en az bir kaydırma yapılmışsa
                        trackList.removeAt(0)  // Liste başından eleman sil
                        count--  // Silme işlemi sonrası, count değerini güncelle
                    }
                    Toast.makeText(this@SpotifySwipeMusic, "Direction Left", Toast.LENGTH_SHORT).show()

                } else if (direction == Direction.Bottom) {
                    Toast.makeText(this@SpotifySwipeMusic, "Direction Bottom", Toast.LENGTH_SHORT).show()
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
        manager.setSwipeableMethod(SwipeableMethod.Manual)
        manager.setOverlayInterpolator(LinearInterpolator())
        adapter = CardStackAdapter(addList())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator()
    }

    private fun loadDataAndSetupCards() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                // Veri yükleme tamamlandığında, kartları kurun
                if (SpotifyApiManager.trackList.isNotEmpty()) {
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
        if (SpotifyApiManager.trackList.isNotEmpty()) {
            val currentTrack = SpotifyApiManager.trackList[count]
            itemsList.add(ItemModel(currentTrack.imageUri, currentTrack.name, currentTrack.artistName, "Current Track"))
        } else {
            // Liste boşsa, bir hata mesajı göster veya uygun bir işlem yap
            Log.e(TAG, "Track listesi boş.")
            // Burada gerekiyorsa veri yükleme işlemini tekrar başlatabilirsiniz veya kullanıcıya bilgi verebilirsiniz.
        }

        return itemsList
    }

    private fun paginate(){
        val old: List<ItemModel> = adapter.getItems()
        val baru = ArrayList(addList())
        val callback = CardStackCallback(old, baru)
        val hasil: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)
        adapter.setItems(baru)
        hasil.dispatchUpdatesTo(adapter)
    }
}