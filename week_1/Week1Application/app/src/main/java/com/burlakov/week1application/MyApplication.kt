package com.burlakov.week1application

import android.app.Application
import com.burlakov.week1application.api.FlickrApi
import com.burlakov.week1application.models.User
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin


class MyApplication : Application() {

    companion object {
        var curUser: User? = null
    }

    override fun onCreate() {
        super.onCreate()

        FlickrApi.configureRetrofit()
        startKoin {
            androidContext(this@MyApplication)
            loadKoinModules(listOf(appModules, repositoryModule, databaseModule))
        }
    }
}