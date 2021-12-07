package com.burlakov.week1application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.burlakov.week1application.api.FlickrApiConstants
import com.burlakov.week1application.models.User
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import java.io.File


class MyApplication : Application() {

    companion object {
        var curUser: User? = null

        fun curUserIsSingIn(): Boolean {
            return curUser?.userId != null
        }
        
        lateinit var internalDir : File

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
        
        internalDir = filesDir
    }
}