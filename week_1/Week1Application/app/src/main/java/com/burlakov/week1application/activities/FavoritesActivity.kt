package com.burlakov.week1application.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.R
import com.burlakov.week1application.adapters.PhotoDeleteButtonAdapter
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.util.DeleteButtonItemCallback
import com.burlakov.week1application.util.FavoritesUtil
import com.burlakov.week1application.viewmodels.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    private val favoritesViewModel: FavoritesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoritesViewModel.favorites.observe(this, {
            val photo: MutableList<SavedPhoto> = mutableListOf()
            photo.addAll(it)
            val list = FavoritesUtil.setHeaders(photo)
            val adapter = PhotoDeleteButtonAdapter(list, favoritesViewModel)
            recyclerView.adapter = adapter
            val deleteItemCallback = DeleteButtonItemCallback(list, adapter, favoritesViewModel)
             val itemTouchHelper = ItemTouchHelper(deleteItemCallback)
             itemTouchHelper.attachToRecyclerView(recyclerView)
        })

    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.getFavorites()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}