package com.burlakov.week1application.util

import android.annotation.SuppressLint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FilesUtil {
    companion object {
        @SuppressLint("SimpleDateFormat")
        @Throws(IOException::class)
        suspend fun createImageFile(filesDir: File): File {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir = File(filesDir, "Images")
            storageDir.mkdir()
            return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
        }
    }
}