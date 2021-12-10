package com.burlakov.week1application.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.BuildConfig
import com.burlakov.week1application.R
import com.burlakov.week1application.adapters.PhotoAdapter
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.util.NetworkUtil
import com.burlakov.week1application.viewmodels.MapSearchResultViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapSearchResultFragment : Fragment() {
    companion object {
        const val LAT = BuildConfig.APPLICATION_ID + ".extra.LAT"
        const val LON = BuildConfig.APPLICATION_ID + ".extra.LON"
    }

    private val mapSearchResultViewModel: MapSearchResultViewModel by viewModel()
    lateinit var recyclerView: RecyclerView
    private var photosList: MutableList<SavedPhoto> = mutableListOf()
    private var adapter: PhotoAdapter = PhotoAdapter(photosList)
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_map_search_result, container, false)
        recyclerView = root.findViewById(R.id.recycler)
        progressBar = root.findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastCompletelyVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (lastCompletelyVisibleItemPosition == recyclerView.adapter?.itemCount?.minus(1) ?: -1
                    && mapSearchResultViewModel.searchResult.value?.photos?.hasNext() == true && !progressBar.isVisible
                    && NetworkUtil.checkConnection(requireContext())
                ) {
                    progressBar.visibility = ProgressBar.VISIBLE
                    mapSearchResultViewModel.searchNext()
                }
            }
        })
        recyclerView.adapter = adapter

        if (NetworkUtil.checkConnection(requireContext())) {
            arguments?.getString(LAT)?.let { arguments?.getString(LON)?.let { it1 ->
                progressBar.visibility = ProgressBar.VISIBLE
                mapSearchResultViewModel.searchPhotos(it, it1) } }
        }

        mapSearchResultViewModel.searchResult.observe(this, {
            val text = it.photos.searchText
            if (it.photos.page == 1) {
                photosList.clear()
                photosList.addAll(it.getSavedPhotos(text))
            } else {
                photosList.addAll(it.getSavedPhotos(text))
            }
            progressBar.visibility = ProgressBar.INVISIBLE
            adapter.notifyDataSetChanged()
        })

        return root
    }

}