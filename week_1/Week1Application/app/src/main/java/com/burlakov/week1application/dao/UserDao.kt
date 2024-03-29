package com.burlakov.week1application.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.burlakov.week1application.models.*

@Dao
interface UserDao {

    @Query("SELECT * FROM User where userId = :userId")
    fun findById(userId: Long): User?

    @Query("SELECT * FROM User where username = :username")
    fun findByUsername(username: String): User?

    @Query("SELECT * FROM User")
    fun findAll(): List<User>

    @Insert()
    fun add(user: User): Long

    @Query("Update User set lastSearchText = :lastSearchText where userId = :userId")
    fun saveLastSearchText(lastSearchText: String, userId: Long)
}