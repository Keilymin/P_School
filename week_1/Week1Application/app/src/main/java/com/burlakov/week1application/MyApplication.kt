package com.burlakov.week1application

import android.app.Application
import com.burlakov.week1application.api.FlickrApi
import okhttp3.logging.HttpLoggingInterceptor


class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        FlickrApi.configureRetrofit()
    }
}