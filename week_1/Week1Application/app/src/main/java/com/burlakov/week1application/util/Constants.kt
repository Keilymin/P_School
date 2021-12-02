package com.burlakov.week1application.util

import android.annotation.SuppressLint
import android.os.Environment
import com.burlakov.week1application.MyApplication
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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
    }
}