package com.burlakov.week1application

import android.app.Application
import android.content.Context
import com.burlakov.week1application.models.User
import com.burlakov.week1application.util.UserUtil
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

        fun checkSavedUserState(context: Context): Boolean {
            curUser = UserUtil.getUser(context)
            return curUser != null
        }

        lateinit var internalDir: File

        fun logOut(context: Context) {
            curUser = null
            UserUtil.logOut(context)
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

        internalDir = filesDir
    }
}