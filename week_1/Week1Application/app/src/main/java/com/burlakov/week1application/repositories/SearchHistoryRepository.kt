package com.burlakov.week1application.repositories

import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.MyApplication.Companion.curUserIsSingIn
import com.burlakov.week1application.dao.SearchHistoryDao
import com.burlakov.week1application.models.SearchText
import com.burlakov.week1application.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchHistoryRepository(private val searchHistoryDao: SearchHistoryDao) {
    suspend fun save(searchText: SearchText) {
        withContext(Dispatchers.IO) {
            searchHistoryDao.add(searchText)
        }
    }

    suspend fun getHistory(user: User): List<SearchText> {
        return searchHistoryDao.getUserHistoryByUserId(user.userId!!).savedPhotos
    }
}