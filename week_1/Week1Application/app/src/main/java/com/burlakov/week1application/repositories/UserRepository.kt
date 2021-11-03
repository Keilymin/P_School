package com.burlakov.week1application.repositories

import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.dao.UserDao
import com.burlakov.week1application.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun logIn(username: String) : Boolean {
        withContext(Dispatchers.IO) {
            val u = userDao.findByUsername(username)
            if (u == null) {
                userDao.add(User(username))
            }
            curUser = u
        }
        return true
    }

    fun saveSearchText(text: String) {
        curUser?.userId?.let { userDao.saveLastSearchText(text, it) }
    }
}
