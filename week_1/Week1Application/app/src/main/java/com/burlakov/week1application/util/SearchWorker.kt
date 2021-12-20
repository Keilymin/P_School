package com.burlakov.week1application.util

import android.content.Context
import android.graphics.Bitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.burlakov.week1application.api.PhotoService
import com.burlakov.week1application.dao.NotificationPhotoDao
import com.burlakov.week1application.models.NotificationPhoto
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.notification.AutoSearchNotification
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams), KoinComponent {
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    lateinit var autoSearch: AutoSearchNotification
    lateinit var searchText: String

    private val photoService by inject<PhotoService>()
    private val notificationPhotoDao by inject<NotificationPhotoDao>()

    override suspend fun doWork(): Result {

        inputData.getString(SEARCH_TEXT)?.let {
            searchText = it
            autoSearch = AutoSearchNotification(appContext)
            val searchRes = photoService.search(searchText, 1)
            val saved = searchRes.getSavedPhotos(searchText)
            val bitmap: Bitmap =
                Glide.with(appContext).asBitmap().load(saved[1].photoUrl).submit().get()
            autoSearch.build(searchText, saved.size, bitmap)

            notificationPhotoDao.deleteAll()
            for (i: SavedPhoto in saved) {
                notificationPhotoDao.add(NotificationPhoto(i.searchText, i.photoUrl))
            }

            autoSearch.show()

            return Result.success()
        }

        return Result.failure()
    }
}

