package com.burlakov.week1application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.models.Photos
import com.burlakov.week1application.models.SearchResult
import com.burlakov.week1application.repositories.PhotoRepository
import kotlinx.coroutines.launch

class MapSearchResultViewModel(private val photoRepository: PhotoRepository) : ViewModel() {

    var photos: Photos? = null
    private lateinit var latitude: String
    private lateinit var longitude: String

    val searchResult: LiveData<SearchResult>
        get() = _searchResult

    private val _searchResult = MutableLiveData<SearchResult>()

    fun searchPhotos(lat: String, lon: String) = viewModelScope.launch {
        val sr = photoRepository.searchOnMap(lat, lon, 1)
        sr.photos.searchText = "$lat\n$lon"
        latitude = lat
        longitude = lon
        _searchResult.value = sr
        photos = searchResult.value?.photos
    }

    fun searchNext() = viewModelScope.launch {
        val sr = photoRepository.searchOnMap(longitude, latitude, photos!!.page + 1)
        sr.photos.searchText = photos!!.searchText
        _searchResult.value = sr
        photos = searchResult.value?.photos
    }

}