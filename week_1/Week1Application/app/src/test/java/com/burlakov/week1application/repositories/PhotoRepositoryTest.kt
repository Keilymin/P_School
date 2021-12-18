package com.burlakov.week1application.repositories

import com.burlakov.week1application.BaseTest
import com.burlakov.week1application.api.PhotoService
import com.burlakov.week1application.dao.SavedPhotoDao
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.models.SearchResult
import com.burlakov.week1application.models.UserSavedPhotos
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PhotoRepositoryTest : BaseTest() {

    private lateinit var photoRepository: PhotoRepository


    @MockK
    private lateinit var photoDao: SavedPhotoDao

    @MockK
    private lateinit var photoService: PhotoService


    @Before
    override fun init() {
        super.init()
        photoRepository = PhotoRepository(photoDao, photoService)
    }

    @Test
    fun `search test`() = runBlocking {
        coEvery { photoService.search(searchText, page) } returns SearchResult(photos)
        val res = photoRepository.search(searchText, page)

        coVerify { photoService.search(searchText, page) }
        assertEquals(SearchResult(photos), res)
    }

    @Test
    fun `searchOnMap test`() = runBlocking {
        coEvery { photoRepository.searchOnMap("1", "1", 1) } returns SearchResult(photos)
        val res = photoRepository.searchOnMap("1", "1", 1)

        coVerify { photoRepository.searchOnMap("1", "1", 1) }
        assertEquals(SearchResult(photos), res)
    }

    @Test
    fun `addToFavorites test`() = runBlocking {
        val savedPh = SavedPhoto(user.userId!!, searchText, url)
        photoRepository.addToFavorites(savedPh)

        coVerify { photoDao.add(savedPh) }
    }

    @Test
    fun `isImageSaved exist`() = runBlocking {
        every { photoDao.exist(user.userId!!, url) } returns SavedPhoto(
            user.userId!!,
            searchText,
            url
        )
        val res = photoRepository.isImageSaved(user.userId!!, url)

        coVerify { photoDao.exist(user.userId!!, url) }
        assertEquals(true, res)
    }

    @Test
    fun `isImageSaved not exist`() = runBlocking {
        every { photoDao.exist(user.userId!!, url) } returns null
        val res = photoRepository.isImageSaved(user.userId!!, url)

        coVerify { photoDao.exist(user.userId!!, url) }
        assertEquals(false, res)
    }

    @Test
    fun `deleteFromFavorite test`() = runBlocking {
        photoRepository.deleteFromFavorite(user.userId!!, url)

        coVerify { photoDao.deleteFavorite(user.userId!!, url) }
    }

    @Test
    fun `getFavorites test`() = runBlocking {
        val userSavedPh = UserSavedPhotos(user, listOf())
        coEvery { photoDao.getUserSavedPhotosByUserId(user.userId!!) } returns userSavedPh
        val res = photoRepository.getFavorites(user.userId!!)

        coVerify { photoDao.getUserSavedPhotosByUserId(user.userId!!) }
        assertEquals(userSavedPh, res)
    }
}