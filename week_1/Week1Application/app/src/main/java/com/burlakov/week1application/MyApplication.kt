package com.burlakov.week1application

import android.app.Application
import com.burlakov.week1application.api.FlickrApiConstants
import com.burlakov.week1application.models.User
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin


class MyApplication : Application() {

    companion object {
        var curUser: User? = null

        fun curUserIsSingIn(): Boolean {
            return if (curUser != null) {
                curUser!!.userId != null
            } else false
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            loadKoinModules(
                listOf(
                    appModules, repositoryModule, databaseModule, netModule,
                    apiModule
                )
            )
        }
    }
}