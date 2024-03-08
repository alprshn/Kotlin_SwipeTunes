package com.example.kotlin_spotify_random_like_app

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CardStackAdapter : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    private lateinit var items: List<ItemModel>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}