package com.burlakov.week1application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class GalleryViewModel : ViewModel() {
    val gallery: LiveData<MutableList<File>>
        get() = _gallery

    private var _gallery = MutableLiveData<MutableList<File>>()

    fun getAllStorageImage(filesDir: File, externalStoragePublicDirectory : File, grantedStorage : Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            val files = mutableListOf<File>()

            File(filesDir.path + "/Images").walkTopDown().filter {
                it.isFile
            }.forEach {
                files.add(it)
            }
            if (grantedStorage) {
                File(externalStoragePublicDirectory.path + "/P_School").walkTopDown().filter {
                    it.isFile
                }.forEach {
                    files.add(it)
                }
            }
            files.sortByDescending { it.name }
            _gallery.postValue(files)
        }

    fun deleteFile(file: File) = viewModelScope.launch(Dispatchers.IO) {
        file.delete()
    }

}