package com.burlakov.week1application.activities



import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burlakov.week1application.R
import com.burlakov.week1application.adapters.PhotoAdapter
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.util.NetworkUtil
import com.burlakov.week1application.viewmodels.MainViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var map: Button
    private lateinit var photo: Button
    private lateinit var search: Button
    private lateinit var history: Button
    private lateinit var gallery: Button
    private lateinit var settings: Button
    private lateinit var favorites: Button
    private lateinit var searchText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private val mainViewModel: MainViewModel by viewModel()
    private var text = ""
    private var photosList: MutableList<SavedPhoto> = mutableListOf()
    private var adapter: PhotoAdapter = PhotoAdapter(photosList)
    private lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        map = findViewById(R.id.map)
        photo = findViewById(R.id.photo)
        search = findViewById(R.id.search)
        history = findViewById(R.id.history)
        gallery = findViewById(R.id.gallery)
        settings = findViewById(R.id.settings)
        favorites = findViewById(R.id.favorites)
        searchText = findViewById(R.id.editTextName)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastCompletelyVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (lastCompletelyVisibleItemPosition == recyclerView.adapter?.itemCount?.minus(1) ?: -1
                    && mainViewModel.photos?.hasNext() == true && !progressBar.isVisible
                    && NetworkUtil.checkConnection(this@MainActivity)
                ) {
                    progressBar.visibility = ProgressBar.VISIBLE
                    mainViewModel.searchNext()
                }
            }
        })
        recyclerView.adapter = adapter

        search.setOnClickListener {

            if (searchText.text.toString().trim()
                    .isNotEmpty() && NetworkUtil.checkConnection(this)
            ) {

                text = searchText.text.toString()

                searchText.setText("")
                progressBar.visibility = ProgressBar.VISIBLE
                mainViewModel.searchPhotos(text)

            }

        }

        mainViewModel.searchResult.observe(this, {
            text = it.photos.searchText
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
        map.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
        gallery.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }
        settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        val dialogClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val uri = Uri.fromFile(File(path))
                        UCrop.of(uri, uri)
                            .start(this)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

        val getContent =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
                if (success) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage(getString(R.string.edit_photo))
                        .setPositiveButton(getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.no), dialogClickListener).show()
                } else{
                    File(path).delete()
                }
            }

        photo.setOnClickListener {
            mainViewModel.createImageFile()
        }
        mainViewModel.savedImage.observe(this) {
            val image = it
            path = image.absolutePath
            val photoURI = FileProvider.getUriForFile(
                this,
                applicationContext.packageName.toString() + ".provider",
                image
            )
            getContent.launch(photoURI)
        }


    }

    override fun onPause() {
        super.onPause()
        mainViewModel.saveSearchText(searchText.text.toString())

    }
}