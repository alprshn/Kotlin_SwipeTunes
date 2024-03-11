package com.example.kotlin_spotify_random_like_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CardStackAdapter(private var items: List<ItemModel>) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    fun getItems(): List<ItemModel> {
        return items
    }

    fun setItems(items: List<ItemModel>) {
        this.items = items
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.item_image)
        private val nama: TextView = itemView.findViewById(R.id.item_name)
        private val usia: TextView = itemView.findViewById(R.id.item_age)
        private val kota: TextView = itemView.findViewById(R.id.item_city)

        fun setData(data: ItemModel) {
        Picasso.get()
            .load(data.getImage())
            .fit()
            .centerCrop()
            .into(image)
            nama.text = data.getNama()
            usia.text = data.getUsia()
            kota.text = data.getKota()

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
        holder.setData(items[position])
    }


}