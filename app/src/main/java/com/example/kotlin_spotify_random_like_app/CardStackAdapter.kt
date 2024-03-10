package com.example.kotlin_spotify_random_like_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardStackAdapter : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    private lateinit var items: List<ItemModel>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var image:ImageView
        lateinit var nama : TextView
        lateinit var usia : TextView
        lateinit var kotia : TextView

        fun setData(get: ItemModel) {
            image = itemView.findViewById(R.id.item_image)
            nama = itemView.findViewById(R.id.item_name)
            kotia = itemView.findViewById(R.id.item_city)
            usia = itemView.findViewById(R.id.item_age)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view: View = inflater.inflate(R.layout.item_card,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items.get(position))
    }
}