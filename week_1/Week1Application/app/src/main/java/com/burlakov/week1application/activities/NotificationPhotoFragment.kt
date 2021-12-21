package com.burlakov.week1application.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.R
import com.burlakov.week1application.adapters.PhotoAdapter
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.viewmodels.MapSearchResultViewModel
import com.burlakov.week1application.viewmodels.NotificationPhotoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationPhotoFragment : Fragment() {

    private val notificationPhotoViewModel: NotificationPhotoViewModel by viewModel()
    lateinit var recyclerView: RecyclerView
    private var photosList: MutableList<SavedPhoto> = mutableListOf()
    private var adapter: PhotoAdapter = PhotoAdapter(photosList)
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notification_photo, container, false)
        recyclerView = root.findViewById(R.id.recycler)
        progressBar = root.findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        notificationPhotoViewModel.getPhoto()
        progressBar.visibility = ProgressBar.VISIBLE

        notificationPhotoViewModel.notificationPhoto.observe(this) {
            photosList.clear()
            photosList.addAll(it)
            progressBar.visibility = ProgressBar.INVISIBLE
            adapter.notifyDataSetChanged()
        }


        return root
    }
}