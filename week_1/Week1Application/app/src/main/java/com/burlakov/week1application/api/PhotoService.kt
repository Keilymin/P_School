package com.burlakov.week1application.api

import com.burlakov.week1application.models.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {


    @GET("/services/rest/?method=flickr.photos.search&api_key=${FlickrApi.api_key}&format=${FlickrApi.format}&nojsoncallback=1")
    suspend fun search(@Query("text") text: String): SearchResult
}