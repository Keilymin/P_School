package com.burlakov.week1application.repositories


import com.burlakov.week1application.models.SearchResult

import com.burlakov.week1application.api.PhotoService
import com.burlakov.week1application.dao.SavedPhotoDao
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.models.UserSavedPhotos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoRepository(private val photoDao: SavedPhotoDao, private val photoService: PhotoService) {
    suspend fun search(text: String, page: Int): SearchResult {
        return photoService.search(text, page)
    }

    suspend fun searchOnMap(lat: String, lon: String, page: Int): SearchResult {
        return photoService.searchOnMap(lat, lon, page)
    }

    suspend fun addToFavorites(savedPhoto: SavedPhoto) {
        withContext(Dispatchers.IO) {
            photoDao.add(savedPhoto)
        }
    }

    fun isImageSaved(photoUserId: Long, photoUrl: String): Boolean {
        return photoDao.exist(photoUserId, photoUrl) != null
    }

    suspend fun deleteFromFavorite(userId: Long, photoUrl: String) {
        withContext(Dispatchers.IO) {
            photoDao.deleteFavorite(userId, photoUrl)
        }
    }

    suspend fun getFavorites(id: Long): UserSavedPhotos {
        return photoDao.getUserSavedPhotosByUserId(id)
    }
}