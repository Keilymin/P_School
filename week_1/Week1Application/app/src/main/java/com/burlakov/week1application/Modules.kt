package com.burlakov.week1application


import android.app.Application
import androidx.room.Room
import com.burlakov.week1application.api.FlickrApiConstants
import com.burlakov.week1application.api.PhotoService
import com.burlakov.week1application.dao.ApplicationDatabase
import com.burlakov.week1application.dao.SavedPhotoDao
import com.burlakov.week1application.dao.SearchHistoryDao
import com.burlakov.week1application.dao.UserDao
import com.burlakov.week1application.repositories.PhotoRepository
import com.burlakov.week1application.repositories.SearchHistoryRepository
import com.burlakov.week1application.repositories.UserRepository
import com.burlakov.week1application.viewmodels.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModules = module {
    viewModel { LogInViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { ImageViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { MapSearchResultViewModel(get()) }
    viewModel { GalleryViewModel() }
}
val repositoryModule = module {
    single { UserRepository(get()) }
    single { PhotoRepository(get(), get()) }
    single { SearchHistoryRepository(get()) }
}
val netModule = module {

    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FlickrApiConstants.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { provideHttpLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
}

val apiModule = module {
    fun providePhotoService(retrofit: Retrofit): PhotoService? {
        return retrofit.create(PhotoService::class.java)
    }

    single { providePhotoService(get()) }
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

    fun providePhotoDao(database: ApplicationDatabase): SavedPhotoDao {
        return database.savedPhotoDao
    }

    fun provideSearchDao(database: ApplicationDatabase): SearchHistoryDao {
        return database.searchHistoryDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideUserDao(get()) }
    single { providePhotoDao(get()) }
    single { provideSearchDao(get()) }
}