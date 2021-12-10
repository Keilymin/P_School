package com.burlakov.week1application.activities


import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.burlakov.week1application.BuildConfig
import com.burlakov.week1application.R
import com.burlakov.week1application.util.PermissionUtil
import com.burlakov.week1application.viewmodels.ImageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class ImageFragment : Fragment() {

    companion object {
        const val PHOTO_URL = BuildConfig.APPLICATION_ID + ".extra.PHOTO_URL"
        const val SEARCH_TEXT = BuildConfig.APPLICATION_ID + ".extra.SEARCH_TEXT"
        const val FILE_PATH = BuildConfig.APPLICATION_ID + ".extra.FILE_PATH"
    }

    lateinit var image: ImageView
    lateinit var headerText: TextView
    lateinit var favorite: Button
    lateinit var save: Button
    lateinit var imageUrl: String
    private val imageViewModel: ImageViewModel by viewModel()
    private val REQUEST_STORAGE_PERMISSION = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_image, container, false)

        image = root.findViewById(R.id.imageView)
        headerText = root.findViewById(R.id.textView)
        favorite = root.findViewById(R.id.favorite)
        save = root.findViewById(R.id.save)

        if (arguments?.getString(PHOTO_URL) != null &&
            arguments?.getString(SEARCH_TEXT) != null
        ) {
            val text = arguments?.getString(SEARCH_TEXT)!!
            imageUrl = arguments?.getString(PHOTO_URL)!!
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
                if (PermissionUtil.checkStoragePermission(
                        requireContext(),
                        this,
                        REQUEST_STORAGE_PERMISSION
                    )
                ) {
                    imageViewModel.saveToStorage(imageUrl, requireContext())
                }

            }
            imageViewModel.saved.observe(this, {
                if (it) {
                    favorite.setText(R.string.delete_from_favorites)
                    favorite.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.image_favorite
                        )
                    )
                } else {
                    favorite.setText(R.string.save_to_favorites)
                    favorite.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.teal_700
                        )
                    )
                }
            })

            imageViewModel.savedToStorage.observe(this) {
                if (it) {
                    Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else if (arguments?.getString(FILE_PATH) != null) {
            val file = File(arguments?.getString(FILE_PATH)!!)
            save.isVisible = false
            favorite.isVisible = false
            Glide.with(requireContext()).load(file).into(image)
        }


        return root
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
                    imageViewModel.saveToStorage(imageUrl, requireContext())
                }
            }
            else -> {
            }
        }
    }

}