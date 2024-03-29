package com.burlakov.week1application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.MyApplication.Companion.curUserIsSingIn
import com.burlakov.week1application.models.SearchText
import com.burlakov.week1application.repositories.SearchHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRepository: SearchHistoryRepository) : ViewModel() {
    val history: LiveData<List<SearchText>>
        get() = _history

    private var _history = MutableLiveData<List<SearchText>>()


    fun getHistory() = viewModelScope.launch(Dispatchers.IO) {
        if (curUserIsSingIn()) {
            val searchList =
                historyRepository.getHistory(curUser!!).sortedByDescending { it.searchId }
            _history.postValue(searchList)
        }
    }

}