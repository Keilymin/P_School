package com.burlakov.week1application.viewmodels

import com.burlakov.week1application.BaseTest
import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.models.Favorites
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.models.SearchText
import com.burlakov.week1application.models.UserSavedPhotos
import com.burlakov.week1application.repositories.PhotoRepository
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoritesViewModelTest : BaseTest() {


    @MockK
    private lateinit var photoRepository: PhotoRepository

    private lateinit var favoritesViewModel: FavoritesViewModel

    private lateinit var correctFavRes: MutableList<Favorites>

    @Before
    override fun init() {
        super.init()
        curUser = user

        correctFavRes = mutableListOf(
            SearchText(curUser!!.userId!!, "A"),
            SavedPhoto(curUser!!.userId!!, "A", url),
            SearchText(curUser!!.userId!!, "B"),
            SavedPhoto(curUser!!.userId!!, "B", url),
            SearchText(curUser!!.userId!!, "C"),
            SavedPhoto(curUser!!.userId!!, "C", url)
        )
    }

    private fun emptyUserSavedPhotos() {
        coEvery { photoRepository.getFavorites(curUser!!.userId!!) } returns UserSavedPhotos(
            curUser!!,
            listOf()
        )
        favoritesViewModel = FavoritesViewModel(photoRepository)
    }

    private fun userSavedPhotos() {
        coEvery { photoRepository.getFavorites(curUser!!.userId!!) } returns UserSavedPhotos(
            curUser!!,
            listOf(
                SavedPhoto(curUser!!.userId!!, "C", url),
                SavedPhoto(curUser!!.userId!!, "A", url),
                SavedPhoto(curUser!!.userId!!, "B", url)
            )
        )
        favoritesViewModel = FavoritesViewModel(photoRepository)
    }


    @Test
    fun `init testing`() = runBlockingTest {
        emptyUserSavedPhotos()
        coVerify { photoRepository.getFavorites(curUser!!.userId!!) }
        assertEquals(mutableListOf<Favorites>(), favoritesViewModel.favorites.value)
    }

    @Test
    fun `getFavorites correct`() = runBlockingTest {
        userSavedPhotos()
        favoritesViewModel.getFavorites()

        coVerify(exactly = 2) { photoRepository.getFavorites(curUser!!.userId!!) }

        assertEquals(correctFavRes, favoritesViewModel.favorites.value)
    }

    @Test
    fun `getFavorites not correct`() = runBlockingTest {
        userSavedPhotos()
        favoritesViewModel.getFavorites()

        coVerify(exactly = 2) { photoRepository.getFavorites(curUser!!.userId!!) }

        assertNotEquals(mutableListOf<Favorites>(), favoritesViewModel.favorites.value)
    }

    @Test
    fun `user not login`() = runBlockingTest {
        curUser = null
        favoritesViewModel = FavoritesViewModel(photoRepository)

        favoritesViewModel.getFavorites()
        favoritesViewModel.deleteFromFavorites(url)

        coVerify { photoRepository wasNot Called }

    }

    @Test
    fun `deleteFromFavorite test`() = runBlockingTest {
        userSavedPhotos()

        favoritesViewModel.deleteFromFavorites(url)

        coVerify { photoRepository.deleteFromFavorite(curUser?.userId!!, url) }

    }

}