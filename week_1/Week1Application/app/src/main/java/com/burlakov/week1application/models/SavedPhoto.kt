package com.burlakov.week1application.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedPhoto(
    val photoUserId: Long,
    val searchText: String,
    val photoUrl: String
){
    @PrimaryKey(autoGenerate = true)
    var photoId: Long? = null
}