package com.gnnikolov.gitviewer.ui.test

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.gnnikolov.gitviewer.domain.ICommitsRepository
import com.gnnikolov.gitviewer.domain.IGitRepoRepository
import com.gnnikolov.gitviewer.domain.IUserRepository
import com.gnnikolov.gitviewer.ui.common.Async
import com.gnnikolov.gitviewer.ui.navigation.UserDetailsDestination
import com.gnnikolov.gitviewer.ui.userdetails.UserDetailsUiState
import com.gnnikolov.gitviewer.ui.userdetails.UserDetailsViewModel
import com.gnnikolov.gitviewer.ui.userdetails.UserRepos
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailsViewModelTest {

    @MockK
    lateinit var userRepository: IUserRepository

    @MockK
    lateinit var commitsRepository: ICommitsRepository

    @MockK
    lateinit var gitRepoRepository: IGitRepoRepository

    @MockK
    lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: UserDetailsViewModel

    private val fakeUser = Utils.fakeUsers[0]

    private val fakeGitRepos by lazy { Utils.getFakeGitRepos(fakeUser.id) }

    private val userData = UserDetailsDestination(userId = fakeUser.id)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())

        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every {
            savedStateHandle.toRoute<UserDetailsDestination>()
        } returns userData

        coEvery {
            userRepository.getUserForId(userData.userId)
        } returns fakeUser

        coEvery {
            gitRepoRepository.getReposForUser(fakeUser, false)
        } returns fakeGitRepos

        viewModel = UserDetailsViewModel(
            savedStateHandle,
            gitRepoRepository,
            userRepository,
            commitsRepository
        )
    }


    @Test
    fun `uiStateStream initial state`() = runTest {
        // Verify that uiStateStream emits the initial UserDetailsUiState immediately after subscription.
        viewModel.uiStateStream.test {
            val item = awaitItem()
            assertEquals(item.ensureLoadingState(), true)
        }
    }

    @Test
    fun `uiStateStream emits loading then success`() = runTest {
        // Verify that uiStateStream emits loading state initially, then a success state with user name and repos after successful data fetching.
        viewModel.uiStateStream.test {
            assertEquals(awaitItem().ensureLoadingState(), true)
            assertEquals(awaitItem().ensureSuccessfulState(), true)
        }
    }

    @Test
    fun `uiStateStream emits loading then error for user`() = runTest {
        // Verify that uiStateStream emits loading state, then an empty user name if userRepository.getUserForId returns null.
        coEvery {
            gitRepoRepository.getReposForUser(fakeUser, false)
        } returns null
        viewModel.uiStateStream.test {
            assertEquals(awaitItem().ensureLoadingState(), true)
            assertEquals(awaitItem().ensureErrorState(), true)
        }
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    private fun UserDetailsUiState.ensureLoadingState(): Boolean {
        return name == "" && reposState == Async.Loading
    }

    private fun UserDetailsUiState.ensureSuccessfulState(): Boolean {
        return name == fakeUser.name && (reposState as? Async.Success<UserRepos>)?.data?.value?.let { repos ->
            if (repos.size != fakeGitRepos.size)
                return@let false
            else {
                repos.forEachIndexed { idx, item ->
                    val fakeItem = fakeGitRepos[idx]
                    if (fakeItem != item)
                        return@let false
                }
                return@let true
            }
        } == true
    }

    private fun UserDetailsUiState.ensureErrorState(): Boolean {
        return name == fakeUser.name && reposState == Async.Error
    }
}