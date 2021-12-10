package com.burlakov.week1application.activities



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.R
import com.burlakov.week1application.adapters.PhotoAdapter
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.util.NetworkUtil
import com.burlakov.week1application.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private lateinit var search: Button
    private lateinit var searchText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private val mainViewModel: MainViewModel by viewModel()
    private var text = ""
    private var photosList: MutableList<SavedPhoto> = mutableListOf()
    private var adapter: PhotoAdapter = PhotoAdapter(photosList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        search = root.findViewById(R.id.search)
        searchText = root.findViewById(R.id.editTextName)
        progressBar = root.findViewById(R.id.progressBar)
        recyclerView = root.findViewById(R.id.recyclerView)


        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastCompletelyVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (lastCompletelyVisibleItemPosition == recyclerView.adapter?.itemCount?.minus(1) ?: -1
                    && mainViewModel.photos?.hasNext() == true && !progressBar.isVisible
                    && NetworkUtil.checkConnection(requireContext())
                ) {
                    progressBar.visibility = ProgressBar.VISIBLE
                    mainViewModel.searchNext()
                }
            }
        })
        recyclerView.adapter = adapter

        search.setOnClickListener {

            if (searchText.text.toString().trim()
                    .isNotEmpty() && NetworkUtil.checkConnection(requireContext())
            ) {
                progressBar.visibility = ProgressBar.VISIBLE
                mainViewModel.searchPhotos(searchText.text.toString())
                searchText.setText("")
            }

        }

        mainViewModel.searchResult.observe(this, {

            if (it.photos.page == 1) {
                photosList.clear()
                photosList.addAll(it.getSavedPhotos(it.photos.searchText))
                if (text != it.photos.searchText){
                    recyclerView.smoothScrollToPosition(0)
                    text = it.photos.searchText
                }
            } else {
                photosList.addAll(it.getSavedPhotos(text))
            }
            progressBar.visibility = ProgressBar.INVISIBLE
            adapter.notifyDataSetChanged()
        })
        mainViewModel.lastText.observe(this, {
            searchText.setText(it)
        })

        return root
    }


    override fun onPause() {
        super.onPause()
        mainViewModel.saveSearchText(searchText.text.toString())

    }

}