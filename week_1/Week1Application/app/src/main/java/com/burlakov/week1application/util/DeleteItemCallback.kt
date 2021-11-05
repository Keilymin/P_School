package com.burlakov.week1application.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.adapters.PhotoAdapter
import com.burlakov.week1application.models.SavedPhoto

 class DeleteItemCallback(
    private val arrayList: MutableList<SavedPhoto>,
    private val adapter: PhotoAdapter
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        arrayList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

}