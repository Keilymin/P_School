package com.burlakov.week1application.api

import com.burlakov.week1application.models.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {


    @GET("/services/rest/?method=flickr.photos.search&api_key=${FlickrApiConstants.api_key}&format=${FlickrApiConstants.format}&nojsoncallback=1&per_page=20")
    suspend fun search(@Query("text") text: String, @Query("page") page: Int): SearchResult
}