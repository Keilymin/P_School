package com.burlakov.week1application.repositories


import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.MyApplication.Companion.curUserIsSingIn
import com.burlakov.week1application.dao.UserDao
import com.burlakov.week1application.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun logIn(username: String): Boolean {
        withContext(Dispatchers.IO) {
            var u = userDao.findByUsername(username)
            if (u == null) {
                u = registerUser(username)
            }
            curUser = u
        }
        return true
    }

    private fun registerUser(username: String): User {
        val user = User(username)
        val id = userDao.add(User(username))
        user.userId = id
        return user
    }

    suspend fun saveSearchText(text: String) = withContext(Dispatchers.IO) {
        if (curUserIsSingIn()) {
            userDao.saveLastSearchText(text, curUser!!.userId!!)
        }
    }

    suspend fun getLastText(): String {
        return if (curUserIsSingIn()) {
            userDao.findById(curUser!!.userId!!)!!.lastSearchText
        } else ""
    }
}
