package com.burlakov.week1application.models

import androidx.room.Embedded
import androidx.room.Relation


data class UserSearchHistory(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "searchUserId"
    )
    val savedPhotos: List<SearchText>
)