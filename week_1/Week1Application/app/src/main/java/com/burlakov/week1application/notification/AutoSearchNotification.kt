package com.burlakov.week1application.notification

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.burlakov.week1application.R
import com.burlakov.week1application.activities.MenuActivity

class AutoSearchNotification(val context: Context) {

    companion object {
        private const val channelId = "AutoSearch"
        const val channelName = "AutoSearch"

        fun createChannel(activity: Activity, context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
                    .apply {
                        setShowBadge(false)
                    }

                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(true)
                notificationChannel.description =
                    context.getString(R.string.autosearch_notif_description)

                val notificationManager = activity.getSystemService(
                    NotificationManager::class.java
                )
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
    }

    private var builder: NotificationCompat.Builder? = null

    fun build(searchText: String, count: Int, bitmap: Bitmap) {
        val intent = Intent(context, MenuActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtra(MenuActivity.NOTIFICATION_INTENT,true)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        builder = NotificationCompat.Builder(context, channelName)
            .setSmallIcon(R.drawable.cat_logo)
            .setContentTitle(searchText)
            .setContentText(context.getString(R.string.found) + " $count " + context.getString(R.string.photos))
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap)
            .bigLargeIcon(null))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

    fun show() {
        if (builder != null) {
            with(NotificationManagerCompat.from(context)) {
                notify(1, builder!!.build())
            }
        }
    }

}