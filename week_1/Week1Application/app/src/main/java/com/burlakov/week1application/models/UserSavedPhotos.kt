package com.burlakov.week1application.models

import androidx.room.Embedded
import androidx.room.Relation


data class UserSavedPhotos(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "photoUserId",
    )
    val savedPhotos: List<SavedPhoto>
)