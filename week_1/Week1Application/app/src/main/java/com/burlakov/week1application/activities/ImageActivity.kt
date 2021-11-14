package com.burlakov.week1application.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.burlakov.week1application.BuildConfig
import com.burlakov.week1application.R
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

    private val imageViewModel: ImageViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.image_view)

        image = findViewById(R.id.imageView)
        headerText = findViewById(R.id.textView)
        favorite = findViewById(R.id.favorite)

        if (intent.getStringExtra(PHOTO_URL) != null &&
            intent.getStringExtra(SEARCH_TEXT) != null
        ) {
            val text = intent.getStringExtra(SEARCH_TEXT)!!
            val imageUrl = intent.getStringExtra(PHOTO_URL)!!
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

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}