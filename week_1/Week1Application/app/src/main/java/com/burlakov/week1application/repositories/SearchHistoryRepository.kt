package com.burlakov.week1application.repositories

import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.MyApplication.Companion.curUserIsSingIn
import com.burlakov.week1application.dao.SearchHistoryDao
import com.burlakov.week1application.models.SearchText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchHistoryRepository(private val searchHistoryDao: SearchHistoryDao) {
    suspend fun save(searchText: SearchText) {
        withContext(Dispatchers.IO) {
            searchHistoryDao.add(searchText)
        }
    }

    suspend fun getHistory(): List<SearchText> {
        return searchHistoryDao.getUserHistoryByUserId(curUser!!.userId!!)[0].savedPhotos
    }
}