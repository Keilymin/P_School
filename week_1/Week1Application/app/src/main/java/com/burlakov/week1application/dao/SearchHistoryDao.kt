package com.burlakov.week1application.dao

import androidx.room.*
import com.burlakov.week1application.models.SearchText
import com.burlakov.week1application.models.UserSearchHistory

@Dao
interface SearchHistoryDao {

    @Transaction
    @Query("SELECT * FROM User Where userId = :id")
    fun getUserHistoryByUserId(id: Long): List<UserSearchHistory>

    @Delete
    fun delete(searchText: SearchText)

    @Insert
    fun add(searchText: SearchText)
}