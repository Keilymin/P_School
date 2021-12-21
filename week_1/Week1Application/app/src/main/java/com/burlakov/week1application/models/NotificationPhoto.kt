package com.burlakov.week1application.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.burlakov.week1application.MyApplication

@Entity
class NotificationPhoto(
    val searchText: String,
    val photoUrl: String
) {
    @PrimaryKey(autoGenerate = true)
    var photoId: Long? = null

}

fun List<NotificationPhoto>.toSavedPhoto(): MutableList<SavedPhoto> {
    val new = arrayListOf<SavedPhoto>()
    for (i: NotificationPhoto in this) {
        new.add(SavedPhoto(MyApplication.curUser!!.userId!!, i.searchText, i.photoUrl))
    }
    return new
}