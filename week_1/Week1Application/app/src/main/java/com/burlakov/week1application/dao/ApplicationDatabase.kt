package com.burlakov.week1application.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.burlakov.week1application.models.*

@Database(
    entities = [User::class, SavedPhoto::class, SearchText::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val searchHistoryDao: SearchHistoryDao
    abstract val savedPhotoDao: SavedPhotoDao
}