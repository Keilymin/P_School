package com.burlakov.week1application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.MyApplication.Companion.curUserIsSingIn
import com.burlakov.week1application.adapters.PhotoDeleter
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.repositories.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(private val photoRepository: PhotoRepository) : ViewModel(), PhotoDeleter {
    val favorites: LiveData<List<SavedPhoto>>
        get() = _favorites

    private var _favorites = MutableLiveData<List<SavedPhoto>>()

    init {
        getFavorites()
    }

    fun getFavorites(){
        viewModelScope.launch(Dispatchers.IO) {
            if (curUserIsSingIn()) {
                val photos = photoRepository.getFavorites(curUser!!.userId!!)
                _favorites.postValue(photos.savedPhotos.sortedBy { it.searchText })
            }
        }
    }

    override fun deleteFromFavorites(url: String) = viewModelScope.launch {
        if (curUserIsSingIn()) {
            photoRepository.deleteFromFavorite(curUser!!.userId!!, url)
        }
    }
}