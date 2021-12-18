package com.burlakov.week1application.viewmodels


import com.burlakov.week1application.BaseTest
import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.models.Photos
import com.burlakov.week1application.models.SearchResult
import com.burlakov.week1application.models.SearchText
import com.burlakov.week1application.repositories.PhotoRepository
import com.burlakov.week1application.repositories.SearchHistoryRepository
import com.burlakov.week1application.repositories.UserRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest : BaseTest() {


    @MockK
    private lateinit var historyRepository: SearchHistoryRepository

    @MockK
    private lateinit var photoRepository: PhotoRepository

    @MockK
    private lateinit var userRepository: UserRepository

    private lateinit var mainViewModel: MainViewModel


    @MockK
    private lateinit var searchResultCorrect: SearchResult

    @MockK
    private lateinit var searchResultNotCorrect: SearchResult

    @MockK
    private lateinit var searchResultCorrectNext: SearchResult


    @Before
     override fun init() {
       super.init()
        coEvery { userRepository.getLastText() } returns testString
        mainViewModel = MainViewModel(historyRepository, photoRepository, userRepository)
    }

    @Test
    fun `init testing`() = runBlockingTest {
        coVerify { userRepository.getLastText() }
        assertEquals(testString, mainViewModel.lastText.value)
    }

    private fun search(ret: SearchResult) {
        coEvery { photoRepository.search(searchText, page) } returns ret
        every { ret.photos } returns photos
        mainViewModel.searchPhotos(searchText)
    }

    @Test
    fun `searchPhotos correct`() = runBlockingTest {
        curUser = user
        search(searchResultCorrect)
        coVerify { historyRepository.save(SearchText(curUser?.userId!!, searchText)) }
        assertEquals(searchResultCorrect, mainViewModel.searchResult.value)
    }

    @Test
    fun `searchPhotos not correct`() = runBlockingTest {
        curUser = null
        search(searchResultNotCorrect)
        coVerify(exactly = 0) { historyRepository.save(SearchText(user.userId!!, searchText)) }
        assertNotEquals(searchResultCorrect, mainViewModel.searchResult.value)
    }

    @Test
    fun `searchNext correct`() = runBlockingTest {
        curUser = user
        search(searchResultCorrect)

        coEvery { photoRepository.search(searchText, page + 1) } returns searchResultCorrectNext
        val nextPhotos = Photos(listOf(), page + 1, pages)
        every { searchResultCorrectNext.photos } returns nextPhotos

        mainViewModel.searchNext()
        coVerify { historyRepository.save(SearchText(curUser?.userId!!, searchText)) }
        assertEquals(searchResultCorrectNext, mainViewModel.searchResult.value)
    }

    @Test
    fun `searchNext not correct`() = runBlockingTest {
        curUser = null
        search(searchResultCorrect)

        coEvery { photoRepository.search(searchText, page + 1) } returns searchResultCorrectNext
        val nextPhotos = Photos(listOf(), page + 1, pages)
        every { searchResultCorrectNext.photos } returns nextPhotos

        mainViewModel.searchNext()
        coVerify(exactly = 0) { historyRepository.save(SearchText(user.userId!!, searchText)) }
        assertNotEquals(searchResultCorrect, mainViewModel.searchResult.value)
    }

    @Test
    fun `saveSearchText test`() = runBlockingTest {
        mainViewModel.saveSearchText(searchText)
        coVerify { userRepository.saveSearchText(searchText) }
    }

}
