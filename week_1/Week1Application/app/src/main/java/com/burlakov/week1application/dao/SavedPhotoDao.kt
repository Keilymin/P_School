package com.burlakov.week1application.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.models.UserSavedPhotos

@Dao
interface SavedPhotoDao {

    @Transaction
    @Query("SELECT * FROM User Where userId = :id")
    fun getUserSavedPhotosByUserId(id: Long): UserSavedPhotos

    @Delete
    fun delete(savedPhoto: SavedPhoto)

    @Insert
    fun add(savedPhoto: SavedPhoto)

    @Query("SELECT * FROM SavedPhoto Where photoUserId == :userId and photoUrl == :photoUrl")
    fun exist(userId: Long, photoUrl: String) : SavedPhoto?

    @Query("Delete FROM SavedPhoto Where photoUserId == :userId and photoUrl == :photoUrl")
    fun deleteFavorite(userId: Long, photoUrl: String)
}