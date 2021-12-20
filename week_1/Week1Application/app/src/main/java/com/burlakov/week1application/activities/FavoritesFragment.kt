package com.burlakov.week1application.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.R
import com.burlakov.week1application.adapters.PhotoDeleteButtonAdapter
import com.burlakov.week1application.util.DeleteButtonItemCallback
import com.burlakov.week1application.viewmodels.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    private val favoritesViewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)

        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        favoritesViewModel.getFavorites()

        favoritesViewModel.favorites.observe(this, {
            val adapter = PhotoDeleteButtonAdapter(it, favoritesViewModel)
            recyclerView.adapter = adapter
            val deleteItemCallback = DeleteButtonItemCallback(it, adapter, favoritesViewModel)
            val itemTouchHelper = ItemTouchHelper(deleteItemCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        })

        return root
    }

}