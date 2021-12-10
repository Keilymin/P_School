package com.burlakov.week1application.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class MenuViewModel : ViewModel() {
    val savedImage: LiveData<File>
        get() = _savedImage

    private val _savedImage = MutableLiveData<File>()

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile() = viewModelScope.launch(Dispatchers.IO) {
        Constants.internalImageDirectory.mkdir()
        _savedImage.postValue(
            File.createTempFile(
                "JPEG_${Constants.timeStamp}_",
                ".jpg",
                Constants.internalImageDirectory
            )
        )
    }
}