package com.burlakov.week1application.models

import com.burlakov.week1application.api.FlickrApi

data class SearchResult(
    val photos: Photos
) {
    fun getPhotoUrls(): String {
        var str = ""
        for (p: Photo in this.photos.photo) {
            str += "\n\n${FlickrApi.photoUrl}${p.server}/${p.id}_${p.secret}.jpg\n\n"
        }
        return str
    }
}