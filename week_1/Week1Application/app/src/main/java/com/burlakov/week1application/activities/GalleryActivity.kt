package com.burlakov.week1application.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.R
import com.burlakov.week1application.adapters.FilePhotoAdapter
import com.burlakov.week1application.util.DeleteImageFileCallback
import com.burlakov.week1application.util.PermissionUtil
import com.burlakov.week1application.viewmodels.GalleryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryActivity : AppCompatActivity() {

    private val galleryViewModel: GalleryViewModel by viewModel()
    lateinit var recycler: RecyclerView
    private val REQUEST_STORAGE_PERMISSION = 1
    private var grantedStorage = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        supportActionBar?.title = getString(R.string.gallery)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)

        grantedStorage =
            PermissionUtil.checkStoragePermission(this, this, REQUEST_STORAGE_PERMISSION)
        galleryViewModel.getAllStorageImage(
            filesDir,
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            grantedStorage
        )

        galleryViewModel.gallery.observe(this) {
            val adapter = FilePhotoAdapter(it)
            recycler.adapter = adapter
            val deleteItemCallback = DeleteImageFileCallback(it, adapter, galleryViewModel)
            val itemTouchHelper = ItemTouchHelper(deleteItemCallback)
            itemTouchHelper.attachToRecyclerView(recycler)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    galleryViewModel.getAllStorageImage(
                        filesDir,
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                        true
                    )
                }
            }
            else -> {
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}