package com.burlakov.week1application.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val username: String,
    var lastSearchText: String
){
    @PrimaryKey(autoGenerate = true)
    var userId: Long? = null
}