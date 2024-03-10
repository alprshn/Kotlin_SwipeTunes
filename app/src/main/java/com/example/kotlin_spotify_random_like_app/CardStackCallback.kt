package com.example.kotlin_spotify_random_like_app

import androidx.recyclerview.widget.DiffUtil

class CardStackCallback(private val old: List<ItemModel>, private val baru: List<ItemModel>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return baru.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].image == baru[newItemPosition].image
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == baru[newItemPosition]
    }
}