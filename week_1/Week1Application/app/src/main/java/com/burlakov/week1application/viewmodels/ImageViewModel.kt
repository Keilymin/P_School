package com.burlakov.week1application.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.MyApplication.Companion.curUserIsSingIn
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.repositories.PhotoRepository
import com.burlakov.week1application.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


class ImageViewModel(private val photoRepository: PhotoRepository) : ViewModel() {
    val saved: LiveData<Boolean>
        get() = _saved

    private var _saved = MutableLiveData<Boolean>()

    val savedToStorage: LiveData<Boolean>
        get() = _savedToStorage

    private var _savedToStorage = MutableLiveData<Boolean>()


    fun favorite(photoUrl: String, searchText: String) = viewModelScope.launch {
        if (curUserIsSingIn()) {
            photoRepository.addToFavorites(SavedPhoto(curUser!!.userId!!, searchText, photoUrl))
            _saved.value = true
        }
    }

    fun alreadyOnFavorites(photoUrl: String) = viewModelScope.launch {
        if (curUserIsSingIn()) {
            withContext(Dispatchers.IO) {
                _saved.postValue(photoRepository.isImageSaved(curUser!!.userId!!, photoUrl))
            }
        }
    }

    fun removeFromFavorites(photoUrl: String) = viewModelScope.launch {
        if (curUserIsSingIn()) {
            photoRepository.deleteFromFavorite(curUser!!.userId!!, photoUrl)
            _saved.value = false
        }
    }


    @SuppressLint("SimpleDateFormat")
    fun saveToStorage(url: String, context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val img = Glide.with(context).asBitmap().load(url).submit().get()

        if (!Constants.externalPublicImageDirectory.exists()) {
            Constants.externalPublicImageDirectory.mkdirs()
        }
        val file = File(Constants.externalPublicImageDirectory, "JPEG_${Constants.timeStamp}.jpg")

        val fos = FileOutputStream(file)
        img.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()

        _savedToStorage.postValue(true)
    }
}