package com.burlakov.week1application.models


import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.MyApplication.Companion.curUserIsSingIn
import com.burlakov.week1application.api.FlickrApiConstants

data class SearchResult(
    val photos: Photos
) {
    fun getSavedPhotos(searchText: String): MutableList<SavedPhoto> {
        val list: MutableList<SavedPhoto> = mutableListOf()
        if (curUserIsSingIn()) {
            for (p: Photo in photos.photo) {
                list.add(
                    SavedPhoto(
                        curUser!!.userId!!,
                        searchText,
                        "${FlickrApiConstants.photoUrl}${p.server}/${p.id}_${p.secret}.jpg"
                    )
                )
            }
        }
        return list
    }
}