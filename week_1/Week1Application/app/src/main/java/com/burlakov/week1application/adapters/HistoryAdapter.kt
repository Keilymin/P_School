package com.burlakov.week1application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.R
import com.burlakov.week1application.models.SearchText

class HistoryAdapter(private val history: List<SearchText>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var text: TextView = itemView.findViewById(R.id.textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.history_item, parent, false)
        return HistoryAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text = history[position]
        holder.text.text = text.searchText
    }

    override fun getItemCount() = history.size
}