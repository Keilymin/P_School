package com.burlakov.week1application.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.models.UserSavedPhotos

@Dao
interface SavedPhotoDao {

    @Transaction
    @Query("SELECT * FROM User Where userId = :id")
    fun getUserSavedPhotosByUserId(id: Long): LiveData<List<UserSavedPhotos>>

    @Delete
    fun delete(savedPhoto: SavedPhoto)

    @Insert
    fun add(savedPhoto: SavedPhoto)
}