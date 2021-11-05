package com.burlakov.week1application.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.adapters.PhotoDeleteButtonAdapter
import com.burlakov.week1application.adapters.PhotoDeleter
import com.burlakov.week1application.models.Favorites
import com.burlakov.week1application.models.SavedPhoto

class DeleteButtonItemCallback(
    private val arrayList: MutableList<Favorites>,
    private val adapter: PhotoDeleteButtonAdapter,
    private val viewModel: PhotoDeleter
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
        val item = arrayList[position]
        if (item is SavedPhoto) {
            viewModel.deleteFromFavorites(item.photoUrl)
            arrayList.removeAt(position)
            FavoritesUtil.check(arrayList, item.searchText)
            adapter.notifyDataSetChanged()
        }
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder is PhotoDeleteButtonAdapter.HeaderViewHolder) {
            return makeMovementFlags(0, 0);
        } else
            return super.getMovementFlags(recyclerView, viewHolder)
    }

}