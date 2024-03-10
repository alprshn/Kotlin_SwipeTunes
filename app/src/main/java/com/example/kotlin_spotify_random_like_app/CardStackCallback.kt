package com.example.kotlin_spotify_random_like_app

import androidx.recyclerview.widget.DiffUtil

class CardStackCallback: DiffUtil.Callback() {
    private lateinit var  old: List<ItemModel>
    private lateinit var  baru: List<ItemModel>

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return baru.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        TODO("Not yet implemented")
    }
}