package com.burlakov.week1application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.dao.NotificationPhotoDao
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.models.toSavedPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationPhotoViewModel(private val notificationPhotoDao: NotificationPhotoDao) : ViewModel() {
    val notificationPhoto: LiveData<MutableList<SavedPhoto>>
        get() = _notificationPhoto

    private var _notificationPhoto = MutableLiveData<MutableList<SavedPhoto>>()

    fun getPhoto() = viewModelScope.launch(Dispatchers.IO){
        val nPhotos = notificationPhotoDao.getAll()
        _notificationPhoto.postValue(nPhotos.toSavedPhoto())
    }
}