package com.burlakov.week1application.repositories

import com.burlakov.week1application.BaseTest
import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.dao.UserDao
import com.burlakov.week1application.models.User
import io.mockk.Called
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryTest : BaseTest(){


    private lateinit var userRepository: UserRepository


    @MockK
    private lateinit var userDao: UserDao


    @Before
    override fun init() {
        super.init()
        curUser = user
        userRepository = UserRepository(userDao)
    }

    @Test
    fun `login already registered`() = runBlocking {
        val user = User(username)
        user.userId = 1
        every { userDao.findByUsername(username) } returns user

        val res = userRepository.logIn(username)

        verify { userDao.findByUsername(username) }

        verify(exactly = 0) { userDao.add(User(username)) }

        assertEquals(user, curUser)
        assertEquals(true, res)
    }

    @Test
    fun `login not registered`() = runBlocking {
        val user = User(username)
        user.userId = 1

        every { userDao.findByUsername(username) } returns null
        every { userDao.add(User(username)) } returns 1

        val res = userRepository.logIn(username)

        verify { userDao.findByUsername(username) }

        verify { userDao.add(User(username)) }

        assertEquals(user, curUser)
        assertEquals(true, res)
    }

    @Test
    fun `saveSearchText test`() = runBlocking {
        curUser = user

        userRepository.saveSearchText(searchText)

        verify { userDao.saveLastSearchText(searchText, curUser!!.userId!!) }
    }

    @Test
    fun `getLastText test`() = runBlocking {
        curUser = user
        every { userDao.findById(curUser!!.userId!!)!!.lastSearchText } returns searchText

        val res = userRepository.getLastText()

        verify { userDao.findById(curUser!!.userId!!) }
        assertEquals(searchText, res)
    }

    @Test
    fun `user not login`() = runBlocking {
        curUser = null

        userRepository.getLastText()
        userRepository.saveSearchText(searchText)

        coVerify { userDao wasNot Called }
    }

}