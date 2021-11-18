package com.burlakov.week1application.api

import com.burlakov.week1application.models.SearchResult
import com.burlakov.week1application.api.FlickrApiConstants.Companion.api_key
import com.burlakov.week1application.api.FlickrApiConstants.Companion.format
import com.burlakov.week1application.api.FlickrApiConstants.Companion.perPage
import com.burlakov.week1application.api.FlickrApiConstants.Companion.radius
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {


    @GET("/services/rest/?method=flickr.photos.search&api_key=$api_key&format=$format&nojsoncallback=1&per_page=$perPage")
    suspend fun search(@Query("text") text: String, @Query("page") page: Int): SearchResult

    @GET("/services/rest/?method=flickr.photos.search&api_key=$api_key&format=$format&nojsoncallback=1&per_page=$perPage&radius=$radius")
    suspend fun searchOnMap(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("page") page: Int
    ): SearchResult
}