package com.example.kotlin_spotify_random_like_app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeableMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SpotifySwipeMusic : AppCompatActivity() {
    private lateinit var manager:CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter
    private lateinit var spotifyApi: SpotifyApi
    val token = MainActivity.accessToken
    companion object {
        private const val TAG = "SpotifySwipeMusic"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify_swipe_music)
        spotifyApi = SpotifyApi // SpotifyApi nesnesini başlatın
         // MainActivity.Companion.accessToken olarak da erişebilirsiniz.
        Log.e("tokenSwipe", token.toString())
        val cardStackView: CardStackView = findViewById(R.id.cardStackView)
        manager = CardStackLayoutManager(this, object:CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                Log.d(TAG, "onCardDragging: d=${direction?.name} ratio=$ratio")
            }

            override fun onCardSwiped(direction: Direction?) {
                Log.d(TAG, "onCardSwiped: p=${manager.topPosition} d=$direction")
                if (direction == Direction.Right) {
                    Toast.makeText(this@SpotifySwipeMusic, "Direction Right", Toast.LENGTH_SHORT).show()
                } else if (direction == Direction.Top) {
                    Toast.makeText(this@SpotifySwipeMusic, "Direction Top", Toast.LENGTH_SHORT).show()
                } else if (direction == Direction.Left) {
                    Toast.makeText(this@SpotifySwipeMusic, "Direction Left", Toast.LENGTH_SHORT).show()
                    val randoMusic = RandoMusic(spotifyApi,token.toString())
                    randoMusic.getASong()
                } else if (direction == Direction.Bottom) {
                    Toast.makeText(this@SpotifySwipeMusic, "Direction Bottom", Toast.LENGTH_SHORT).show()
                }

                if (manager.topPosition == adapter.itemCount - 5) {
                    paginate()
                }
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
        manager.setDirections(Direction.FREEDOM)
        manager.setCanScrollHorizontal(true)
        manager.setSwipeableMethod(SwipeableMethod.Manual)
        manager.setOverlayInterpolator(LinearInterpolator())
        adapter = CardStackAdapter(addList())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator()
    }

    private fun addList(): List<ItemModel> {
        val itemsList = ArrayList<ItemModel>()
        val name = intent.getStringExtra("name")
        val artistName = intent.getStringExtra("artistName")
        val imageUri = intent.getStringExtra("imageUri")


        Log.e("deneme",imageUri.toString())
        itemsList.add(ItemModel(imageUri, name.toString(), artistName.toString(),"asd"))
        return  itemsList
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