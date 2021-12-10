package com.burlakov.week1application.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class GalleryViewModel : ViewModel() {
    var granted: Boolean = false

    val gallery: LiveData<MutableList<File>>
        get() = _gallery

    private var _gallery = MutableLiveData<MutableList<File>>()

    fun getAllStorageImage(grantedStorage: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            granted = grantedStorage
            val files = mutableListOf<File>()

            Constants.internalImageDirectory.walkTopDown().filter {
                it.isFile
            }.forEach {
                files.add(it)
            }
            if (grantedStorage) {
                Constants.externalPublicImageDirectory.walkTopDown().filter {
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

    fun deleteAndRefresh(file: File) = viewModelScope.launch(Dispatchers.IO) {
        var list = mutableListOf<File>()
        gallery.value?.let { list.addAll(it) }
        list.remove(file)
        deleteFile(file)
        _gallery.postValue(list)
    }
}