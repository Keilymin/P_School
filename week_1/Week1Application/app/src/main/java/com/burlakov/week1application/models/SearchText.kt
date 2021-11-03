package com.burlakov.week1application.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class SearchText(
    val searchUserId: Long,
    val searchText: String,
){
    @PrimaryKey(autoGenerate = true)
    var searchId: Long? = null
}