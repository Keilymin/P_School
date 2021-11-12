package com.burlakov.week1application.api


import com.burlakov.week1application.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FlickrApiConstants {

    companion object {

        const val baseUrl = "https://api.flickr.com/"
        const val photoUrl = "https://live.staticflickr.com/"
        const val api_key = BuildConfig.SECRET_KEY
        const val format = "json"

    }

}