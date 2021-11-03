package com.burlakov.week1application


import android.app.Application
import androidx.room.Room
import com.burlakov.week1application.dao.ApplicationDatabase
import com.burlakov.week1application.dao.UserDao
import com.burlakov.week1application.repositories.UserRepository
import com.burlakov.week1application.viewmodels.LogInViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    viewModel { LogInViewModel(get()) }

}
val repositoryModule = module {
    single { UserRepository(get()) }
}
val databaseModule = module {

    fun provideDatabase(application: Application): ApplicationDatabase {
        return Room.databaseBuilder(application, ApplicationDatabase::class.java, "mydatabase")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideUserDao(database: ApplicationDatabase): UserDao {
        return database.userDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideUserDao(get()) }
}