package com.burlakov.week1application.activities

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.BuildConfig
import com.burlakov.week1application.R
import com.burlakov.week1application.adapters.PhotoAdapter
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.util.NetworkUtil
import com.burlakov.week1application.viewmodels.MapSearchResultViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapSearchResultActivity : AppCompatActivity() {
    companion object {
        const val LAT = BuildConfig.APPLICATION_ID + ".extra.LAT"
        const val LON = BuildConfig.APPLICATION_ID + ".extra.LON"
    }

    private val mapSearchResultViewModel: MapSearchResultViewModel by viewModel()
    lateinit var recyclerView: RecyclerView
    private var photosList: MutableList<SavedPhoto> = mutableListOf()
    private var adapter: PhotoAdapter = PhotoAdapter(photosList)
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_search_result)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.map_search_result)


        recyclerView = findViewById(R.id.recycler)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastCompletelyVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (lastCompletelyVisibleItemPosition == recyclerView.adapter?.itemCount?.minus(1) ?: -1
                    && mapSearchResultViewModel.photos?.hasNext() == true && !progressBar.isVisible
                    && NetworkUtil.checkConnection(this@MapSearchResultActivity)
                ) {
                    progressBar.visibility = ProgressBar.VISIBLE
                    mapSearchResultViewModel.searchNext()
                }
            }
        })
        recyclerView.adapter = adapter

        if (intent.getStringExtra(LAT) != null &&
            intent.getStringExtra(LON) != null && NetworkUtil.checkConnection(this)
        ) {
            val lat = intent.getStringExtra(LAT)!!
            val lon = intent.getStringExtra(LON)!!
            progressBar.visibility = ProgressBar.VISIBLE
            mapSearchResultViewModel.searchPhotos(lat, lon)
        }

        mapSearchResultViewModel.searchResult.observe(this, {
            val text = it.photos.searchText
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
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}