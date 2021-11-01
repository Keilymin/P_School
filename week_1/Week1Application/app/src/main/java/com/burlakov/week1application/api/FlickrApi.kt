package com.burlakov.week1application.api


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FlickrApi {

    companion object {
        lateinit var photoService : PhotoService

        const val baseUrl = "https://api.flickr.com/"
        const val photoUrl = "https://live.staticflickr.com/"
        const val api_key = "e3c09b7aa90aba9ae0fe92e7ba32b6e9"
        const val format = "json"

        fun configureRetrofit() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()


            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            photoService = retrofit.create(PhotoService::class.java)
        }
    }


}