package com.gnnikolov.gitviewer.ui.test

import app.cash.turbine.test
import com.gnnikolov.gitviewer.domain.IUserRepository
import com.gnnikolov.gitviewer.domain.model.User
import com.gnnikolov.gitviewer.ui.common.Async
import com.gnnikolov.gitviewer.ui.users.Users
import com.gnnikolov.gitviewer.ui.users.UsersViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    @MockK
    lateinit var repository: IUserRepository

    private lateinit var instance: UsersViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `uiState emits Loading and Success state`() = runTest {
        val mockData = listOf(
            User(
                id = "MDQ6VXNlcjE=",
                name = "Go6o",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4"
            )
        )
        val mockUsers = Users(mockData)
        coEvery { repository.getUsers() } returns mockUsers.value
        instance = UsersViewModel(repository,)
        instance.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(Async.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            val successState = awaitItem()
            Assert.assertTrue("State should be Success", successState is Async.Success)
            assertEquals(mockUsers, (successState as Async.Success).data)
            cancelAndConsumeRemainingEvents()
        }
        coVerify(exactly = 1) { repository.getUsers() }
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }
}
