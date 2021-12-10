package com.burlakov.week1application.activities

import android.content.pm.PackageManager
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
import com.burlakov.week1application.adapters.FilePhotoAdapter
import com.burlakov.week1application.util.DeleteImageFileCallback
import com.burlakov.week1application.util.PermissionUtil
import com.burlakov.week1application.viewmodels.GalleryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class GalleryFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel by viewModel()
    lateinit var recycler: RecyclerView
    private val REQUEST_STORAGE_PERMISSION = 1
    private var grantedStorage = false
    private var files: MutableList<File> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        recycler = root.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(context)
        val adapter = FilePhotoAdapter(files)
        recycler.adapter = adapter
        val deleteItemCallback = DeleteImageFileCallback(files, adapter, galleryViewModel)
        val itemTouchHelper = ItemTouchHelper(deleteItemCallback)
        itemTouchHelper.attachToRecyclerView(recycler)

        grantedStorage =
            PermissionUtil.checkStoragePermission(
                requireContext(),
                this,
                REQUEST_STORAGE_PERMISSION
            )

        galleryViewModel.gallery.observe(this) {
            files.clear()
            files.addAll(it)
            adapter.notifyDataSetChanged()
        }
        galleryViewModel.getAllStorageImage(grantedStorage)
        return root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    galleryViewModel.getAllStorageImage(
                        true
                    )
                }
            }
            else -> {
            }
        }
    }

}