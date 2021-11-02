package com.burlakov.week1application.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchText(
    val userId: Long,
    val searchText: String,
){
    @PrimaryKey(autoGenerate = true)
    var searchId: Long? = null
}