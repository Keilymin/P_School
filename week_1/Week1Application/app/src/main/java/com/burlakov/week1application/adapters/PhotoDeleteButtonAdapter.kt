package com.burlakov.week1application.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.burlakov.week1application.R
import com.burlakov.week1application.activities.ImageActivity
import com.burlakov.week1application.models.Favorites
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.models.SearchText
import com.burlakov.week1application.util.FavoritesUtil

class PhotoDeleteButtonAdapter(
    private val favorites: MutableList<Favorites>,
    private val viewModel: PhotoDeleter
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text: TextView = itemView.findViewById(R.id.textView)

    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.imageView)
        val delete: ImageButton = itemView.findViewById(R.id.deleteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.history_item, parent, false)
            HeaderViewHolder(itemView)
        } else {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.photo_delete_item, parent, false)
            ItemViewHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (favorites[position] is SearchText) {
            0
        } else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val favorite = favorites[position]
        if (favorite is SearchText && holder is HeaderViewHolder) {
            holder.text.text = favorite.searchText
        } else if (favorite is SavedPhoto && holder is ItemViewHolder) {
            Glide.with(holder.image.context).load(favorite.photoUrl).into(holder.image)
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, ImageActivity::class.java)
                intent.putExtra(ImageActivity.SEARCH_TEXT, favorite.searchText)
                intent.putExtra(ImageActivity.PHOTO_URL, favorite.photoUrl)
                holder.itemView.context.startActivity(intent)
            }
            holder.delete.setOnClickListener {
                viewModel.deleteFromFavorites(favorite.photoUrl)
                favorites.removeAt(position)
                FavoritesUtil.check(favorites,favorite.searchText)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = favorites.size

}