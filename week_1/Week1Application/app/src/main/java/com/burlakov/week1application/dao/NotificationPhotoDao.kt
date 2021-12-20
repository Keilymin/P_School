package com.burlakov.week1application.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.burlakov.week1application.models.NotificationPhoto

@Dao
interface NotificationPhotoDao {
    @Query("DELETE FROM NotificationPhoto")
    fun deleteAll()

    @Insert
    fun add(notificationPhoto: NotificationPhoto)

    @Query("SELECT * FROM NotificationPhoto")
    fun getAll(): MutableList<NotificationPhoto>
}