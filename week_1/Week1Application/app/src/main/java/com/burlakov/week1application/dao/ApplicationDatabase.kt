package com.burlakov.week1application.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.burlakov.week1application.models.*

@Database(
    entities = [User::class, SavedPhoto::class, UserSavedPhotos::class, SearchText::class, UserSearchHistory::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}