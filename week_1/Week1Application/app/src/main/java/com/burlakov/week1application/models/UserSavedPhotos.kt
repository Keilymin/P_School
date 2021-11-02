package com.burlakov.week1application.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class UserSavedPhotos(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val savedPhotos: List<UserSavedPhotos>
)