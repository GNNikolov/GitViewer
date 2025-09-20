package com.gnnikolov.gitviewer.ui.test

import app.cash.turbine.test
import com.gnnikolov.gitviewer.domain.IUserRepository
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
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    @MockK
    lateinit var repository: IUserRepository

    private lateinit var instance: UsersViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        instance = UsersViewModel(repository)
    }

    @Test
    fun `uiState emits Loading and Success state`() = runTest {
        val mockUsers = Users(Utils.fakeUsers)
        coEvery { repository.getUsers() } returns mockUsers.value
        instance.uiState.test {
            assertEquals(Async.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue("State should be Success", successState is Async.Success)
            assertEquals(mockUsers, (successState as Async.Success).data)
            cancelAndConsumeRemainingEvents()
        }
        coVerify(exactly = 1) { repository.getUsers() }
    }

    @Test
    fun `uiState emits Loading and Error state`() = runTest {
        coEvery { repository.getUsers() } returns emptyList()
        instance.uiState.test {
            assertEquals(Async.Loading, awaitItem())
            assertTrue(awaitItem() is Async.Error)
            cancelAndConsumeRemainingEvents()
        }
        coVerify(exactly = 1) { repository.getUsers() }
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }
}
