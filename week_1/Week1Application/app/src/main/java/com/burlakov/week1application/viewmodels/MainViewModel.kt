package com.burlakov.week1application.viewmodels


import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.MyApplication.Companion.curUserIsSingIn
import com.burlakov.week1application.models.Photos
import com.burlakov.week1application.models.SearchResult
import com.burlakov.week1application.models.SearchText
import com.burlakov.week1application.repositories.PhotoRepository
import com.burlakov.week1application.repositories.SearchHistoryRepository
import com.burlakov.week1application.repositories.UserRepository
import com.burlakov.week1application.util.Constants
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException

class MainViewModel(
    private val historyRepository: SearchHistoryRepository,
    private val photoRepository: PhotoRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var photos: Photos? = null

    val searchResult: LiveData<SearchResult>
        get() = _searchResult

    private val _searchResult = MutableLiveData<SearchResult>()

    val lastText: LiveData<String>
        get() = _lastText

    private var _lastText = MutableLiveData<String>()

    val savedImage: LiveData<File>
        get() = _savedImage

    private val _savedImage = MutableLiveData<File>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _lastText.postValue(userRepository.getLastText())
        }
    }

    fun searchPhotos(searchText: String) = viewModelScope.launch {
        val sr = photoRepository.search(searchText, 1)
        sr.photos.searchText = searchText
        _searchResult.value = sr
        photos = searchResult.value?.photos
        if (curUserIsSingIn()) {
            historyRepository.save(SearchText(curUser!!.userId!!, searchText))
        }
    }

    fun searchNext() = viewModelScope.launch {
        val sr = photoRepository.search(photos!!.searchText, photos!!.page + 1)
        sr.photos.searchText = photos!!.searchText
        _searchResult.value = sr
        photos = searchResult.value?.photos
    }

    fun saveSearchText(searchText: String) = CoroutineScope(Dispatchers.IO).launch {
        userRepository.saveSearchText(searchText)
    }

}