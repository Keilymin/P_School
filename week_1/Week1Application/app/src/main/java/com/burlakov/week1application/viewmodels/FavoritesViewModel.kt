package com.burlakov.week1application.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.MyApplication.Companion.curUserIsSingIn
import com.burlakov.week1application.adapters.PhotoDeleter
import com.burlakov.week1application.models.Favorites
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.repositories.PhotoRepository
import com.burlakov.week1application.util.FavoritesUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(private val photoRepository: PhotoRepository) : ViewModel(), PhotoDeleter {
    val favorites: MutableLiveData<MutableList<Favorites>>
        get() = _favorites

    private var _favorites = MutableLiveData<MutableList<Favorites>>()

    init {
        getFavorites()
    }

    fun getFavorites(){
        viewModelScope.launch(Dispatchers.IO) {
            if (curUserIsSingIn()) {
                val photos = photoRepository.getFavorites(curUser!!.userId!!)
                val photo: MutableList<SavedPhoto> = mutableListOf()
                photo.addAll(photos.savedPhotos.sortedBy { it.searchText })
                val list = FavoritesUtil.setHeaders(photo)
                _favorites.postValue(list)
            }
        }
    }

    override fun deleteFromFavorites(url: String) = viewModelScope.launch {
        if (curUserIsSingIn()) {
            photoRepository.deleteFromFavorite(curUser!!.userId!!, url)
        }
    }
}