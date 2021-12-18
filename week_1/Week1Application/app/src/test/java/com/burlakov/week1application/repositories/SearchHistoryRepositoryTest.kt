package com.burlakov.week1application.repositories

import com.burlakov.week1application.BaseTest
import com.burlakov.week1application.dao.SearchHistoryDao
import com.burlakov.week1application.models.SearchText
import com.burlakov.week1application.models.UserSearchHistory
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchHistoryRepositoryTest : BaseTest() {


    private lateinit var searchHistoryRepository: SearchHistoryRepository

    @MockK
    private lateinit var searchHistoryDao: SearchHistoryDao

    @Before
    override fun init() {
        super.init()
        searchHistoryRepository = SearchHistoryRepository(searchHistoryDao)
    }


    @Test
    fun `save testing`() = runBlocking {
        val search = SearchText(user.userId!!, searchText)
        searchHistoryRepository.save(search)

        verify { searchHistoryDao.add(search) }
    }

    @Test
    fun `getHistory testing`() = runBlocking {
        val search = SearchText(user.userId!!, searchText)
        every { searchHistoryDao.getUserHistoryByUserId(user.userId!!) } returns UserSearchHistory(
            user,
            listOf(search)
        )
        val res = searchHistoryRepository.getHistory(user)

        verify {
            searchHistoryDao.getUserHistoryByUserId(user.userId!!)
            assertEquals(res, listOf(search))
        }
    }
}