package com.burlakov.week1application.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.burlakov.week1application.R
import com.burlakov.week1application.activities.ImageActivity
import com.burlakov.week1application.models.SavedPhoto

class PhotoAdapter(private val photos: List<SavedPhoto>) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text: TextView = itemView.findViewById(R.id.textView)
        val image: ImageView = itemView.findViewById(R.id.imageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photos[position]
        holder.text.text = photo.searchText
        Glide.with(holder.image.context).load(photo.photoUrl).into(holder.image)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ImageActivity::class.java)
            intent.putExtra(ImageActivity.SEARCH_TEXT, photo.searchText)
            intent.putExtra(ImageActivity.PHOTO_URL, photo.photoUrl)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = photos.size
}