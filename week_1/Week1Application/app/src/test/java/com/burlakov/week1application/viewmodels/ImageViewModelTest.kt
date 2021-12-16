package com.burlakov.week1application.viewmodels

import android.content.Context
import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bumptech.glide.Glide
import com.burlakov.week1application.BaseTest
import com.burlakov.week1application.MyApplication
import com.burlakov.week1application.MyApplication.Companion.curUser
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.models.User
import com.burlakov.week1application.repositories.PhotoRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class ImageViewModelTest : BaseTest(){


    private lateinit var imageViewModel: ImageViewModel

    @MockK
    private lateinit var photoRepository: PhotoRepository



    @Before
    override fun init() {
        super.init()
        curUser = user
        imageViewModel = ImageViewModel(photoRepository)
    }

    @Test
    fun `favorite testing`() = runBlockingTest {
        imageViewModel.favorite(url, searchText)

        coVerify { photoRepository.addToFavorites(SavedPhoto(curUser!!.userId!!, searchText, url)) }
        assertEquals(true, imageViewModel.saved.value)
    }

    @Test
    fun `alreadyOnFavorites testing`() = runBlockingTest {
        every { photoRepository.isImageSaved(curUser?.userId!!, url) } returns true
        imageViewModel.alreadyOnFavorites(url)

        coVerify { photoRepository.isImageSaved(curUser?.userId!!, url) }
        assertEquals(true, imageViewModel.saved.value)
    }

    @Test
    fun `removeFromFavorites testing`() = runBlockingTest {
        imageViewModel.removeFromFavorites(url)

        coVerify { photoRepository.deleteFromFavorite(curUser?.userId!!, url) }
        assertEquals(false, imageViewModel.saved.value)
    }

    @Test
    fun `user not login`() = runBlockingTest {
        curUser = null

        imageViewModel.favorite(url, searchText)
        imageViewModel.alreadyOnFavorites(url)
        imageViewModel.removeFromFavorites(url)

        coVerify { photoRepository wasNot Called }
    }
}