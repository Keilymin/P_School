package com.burlakov.week1application.repositories

import com.burlakov.week1application.api.PhotoService
import com.burlakov.week1application.dao.SavedPhotoDao

class PhotoRepository(private val photoDao: SavedPhotoDao, private val photoService: PhotoService) {
}