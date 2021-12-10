package com.burlakov.week1application.viewmodels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.models.SearchResult
import com.burlakov.week1application.repositories.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapSearchResultViewModel(private val photoRepository: PhotoRepository) : ViewModel() {


    private var latitude: String? = null
    private var longitude: String? = null

    val searchResult: LiveData<SearchResult>
        get() = _searchResult

    private val _searchResult = MutableLiveData<SearchResult>()

    fun searchPhotos(lat: String, lon: String) = viewModelScope.launch(Dispatchers.IO) {
        if (latitude != lat && longitude != lon) {
            val sr = photoRepository.searchOnMap(lat, lon, 1)
            sr.photos.searchText = "$lat\n$lon"
            latitude = lat
            longitude = lon
            _searchResult.postValue(sr)
        }
    }

    fun searchNext() = viewModelScope.launch(Dispatchers.IO) {
        val prev = searchResult.value?.photos!!
        val sr = photoRepository.searchOnMap(latitude!!, longitude!!, prev.page + 1)
        sr.photos.searchText = prev.searchText
        _searchResult.postValue(sr)
    }

}