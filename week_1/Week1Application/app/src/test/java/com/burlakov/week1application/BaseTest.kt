package com.burlakov.week1application

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.burlakov.week1application.models.Photos
import com.burlakov.week1application.models.User
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
open class BaseTest {

    val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val username = "Username"

    var user = User(username)

    val testString = "Test"

    val searchText = "cats"

    val page = 1

    val pages = 99

    var photos = Photos(listOf(), page, pages)

    val url = "github"

    @Before
    open fun init() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        user.userId = 1
    }

    @After
    open fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}