package com.burlakov.week1application.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.burlakov.week1application.BuildConfig
import com.burlakov.week1application.R
import com.burlakov.week1application.util.PermissionUtil
import com.burlakov.week1application.viewmodels.ImageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ImageActivity : AppCompatActivity() {

    companion object {
        const val PHOTO_URL = BuildConfig.APPLICATION_ID + ".extra.PHOTO_URL"
        const val SEARCH_TEXT = BuildConfig.APPLICATION_ID + ".extra.SEARCH_TEXT"
    }

    lateinit var image: ImageView
    lateinit var headerText: TextView
    lateinit var favorite: Button
    lateinit var save: Button
    lateinit var imageUrl: String
    private val imageViewModel: ImageViewModel by viewModel()
    private val REQUEST_STORAGE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.image_view)

        image = findViewById(R.id.imageView)
        headerText = findViewById(R.id.textView)
        favorite = findViewById(R.id.favorite)
        save = findViewById(R.id.save)

        if (intent.getStringExtra(PHOTO_URL) != null &&
            intent.getStringExtra(SEARCH_TEXT) != null
        ) {
            val text = intent.getStringExtra(SEARCH_TEXT)!!
            imageUrl = intent.getStringExtra(PHOTO_URL)!!
            headerText.text = text
            Glide.with(this).load(imageUrl).into(image)
            imageViewModel.alreadyOnFavorites(imageUrl)
            favorite.setOnClickListener {
                if (imageViewModel.saved.value == false) {
                    imageViewModel.favorite(imageUrl, text)
                } else {
                    imageViewModel.removeFromFavorites(imageUrl)
                }

            }
            save.setOnClickListener {
                if (PermissionUtil.checkStoragePermission(this, this, REQUEST_STORAGE_PERMISSION)) {
                    imageViewModel.saveToStorage(imageUrl, this)
                }

            }
        }

        imageViewModel.saved.observe(this, {
            if (it) {
                favorite.setText(R.string.delete_from_favorites)
                favorite.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            } else {
                favorite.setText(R.string.save_to_favorites)
                favorite.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700))
            }
        })

        imageViewModel.savedToStorage.observe(this) {
            if (it) {
                Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show()
            }
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
                    imageViewModel.saveToStorage(imageUrl, this)
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