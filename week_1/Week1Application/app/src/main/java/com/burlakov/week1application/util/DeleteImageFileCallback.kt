package com.burlakov.week1application.util


import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.adapters.FilePhotoAdapter
import com.burlakov.week1application.viewmodels.GalleryViewModel
import java.io.File

class DeleteImageFileCallback(
    private val arrayList: MutableList<File>,
    private val adapter: FilePhotoAdapter,
    private val viewModel: GalleryViewModel
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
        viewModel.deleteFile(item)
        arrayList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

}