package com.burlakov.week1application.util

import android.annotation.SuppressLint
import android.os.Environment
import com.burlakov.week1application.MyApplication
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class Constants {
    companion object {
        val timeStamp: String
            @SuppressLint("SimpleDateFormat")
            get() = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val externalPublicImageDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "P_School"
        )
        val internalImageDirectory = File(MyApplication.internalDir, "Images")

        val intervalList = arrayListOf(15L, 30L, 1L, 6L, 1L)
        val timeUnitList = arrayListOf(TimeUnit.MINUTES, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.HOURS, TimeUnit.DAYS)
    }
}