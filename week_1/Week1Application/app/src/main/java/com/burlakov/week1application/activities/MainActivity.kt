package com.burlakov.week1application.activities


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.R
import com.burlakov.week1application.adapters.PhotoAdapter
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.util.DeleteItemCallback
import com.burlakov.week1application.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {


    private lateinit var search: Button
    private lateinit var history: Button
    private lateinit var favorites: Button
    private lateinit var searchText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private val mainViewModel: MainViewModel by viewModel()
    private var text = ""
    private var photosList: MutableList<SavedPhoto> = mutableListOf()
    private var adapter: PhotoAdapter = PhotoAdapter(photosList)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search = findViewById(R.id.search)
        history = findViewById(R.id.history)
        favorites = findViewById(R.id.favorites)
        searchText = findViewById(R.id.editTextName)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastCompletelyVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (lastCompletelyVisibleItemPosition == recyclerView.adapter?.itemCount?.minus(1) ?: -1 && mainViewModel.photos?.hasNext() == true && !progressBar.isVisible) {
                    progressBar.visibility = ProgressBar.VISIBLE
                    mainViewModel.searchNext()
                }
            }
        })
        recyclerView.adapter = adapter
        val deleteItemCallback = DeleteItemCallback(photosList, adapter)
        val itemTouchHelper = ItemTouchHelper(deleteItemCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        search.setOnClickListener {

            if (searchText.text.toString().trim().isNotEmpty()) {

                text = searchText.text.toString()

                searchText.setText("")
                progressBar.visibility = ProgressBar.VISIBLE
                mainViewModel.searchPhotos(text)

            }
        }

        mainViewModel.searchResult.observe(this, {
            if (it.photos.page == 1) {
                photosList.clear()
                photosList.addAll(it.getSavedPhotos(text))
                recyclerView.smoothScrollToPosition(0)
            } else {
                photosList.addAll(it.getSavedPhotos(text))
            }
            progressBar.visibility = ProgressBar.INVISIBLE
            adapter.notifyDataSetChanged()

        })
        mainViewModel.lastText.observe(this, {
            searchText.setText(it)
        })

        history.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        favorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

    }

    override fun onPause() {
        super.onPause()
        mainViewModel.saveSearchText(searchText.text.toString())

    }


}