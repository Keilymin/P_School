package com.burlakov.week1application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.MyApplication.Companion.curUserIsSingIn
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.repositories.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageViewModel(private val photoRepository: PhotoRepository) : ViewModel() {
    val saved: LiveData<Boolean>
        get() = _saved

    private var _saved = MutableLiveData<Boolean>()


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
}