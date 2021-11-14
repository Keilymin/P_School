package com.burlakov.week1application.api


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FlickrApiConstants {

    companion object {

        const val baseUrl = "https://api.flickr.com/"
        const val photoUrl = "https://live.staticflickr.com/"
        const val api_key = "e3c09b7aa90aba9ae0fe92e7ba32b6e9"
        const val format = "json"

    }

}