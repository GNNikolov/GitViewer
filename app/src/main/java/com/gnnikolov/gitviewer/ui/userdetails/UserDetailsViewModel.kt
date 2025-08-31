package com.gnnikolov.gitviewer.ui.userdetails

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.gnnikolov.gitviewer.domain.ICommitsRepository
import com.gnnikolov.gitviewer.domain.IGitRepoRepository
import com.gnnikolov.gitviewer.domain.IUserRepository
import com.gnnikolov.gitviewer.domain.model.Commit
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User
import com.gnnikolov.gitviewer.ui.common.Async
import com.gnnikolov.gitviewer.ui.navigation.UserDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Immutable
data class UserRepos(val value: List<GitRepo>)

@Immutable
data class UserDetailsUiState(
    val name: String = "",
    val reposState: Async<UserRepos> = Async.Loading
)

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: IGitRepoRepository,
    private val userRepository: IUserRepository,
    private val commitsRepository: ICommitsRepository
) : ViewModel() {

    private val userId = savedStateHandle.toRoute<UserDetailsDestination>().userId

    private val repoCommitMap = HashMap<GitRepo, Commit>()

    private val userStream = MutableStateFlow<User?>(null)

    private val userReposStream = MutableStateFlow<Async<UserRepos>>(Async.Loading)

    val uiStateStream: StateFlow<UserDetailsUiState> =
        userStream.combine(userReposStream) { user, reposState ->
            UserDetailsUiState(user?.name ?: "", reposState)
        }.onStart {
            loadRemoteData()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UserDetailsUiState()
        )

    private fun loadRemoteData() {
        viewModelScope.launch {
            val user = userRepository.getUserForId(userId)
            userStream.value = user
            val repos = repository.getReposForUser(user)
            userReposStream.value =
                if (repos != null) Async.Success(UserRepos(repos)) else Async.Error
        }
    }

    fun lastCommitForRepo(model: GitRepo): Flow<Async<Commit?>> = channelFlow {
        repoCommitMap[model]?.let { commit ->
            send(Async.Success(commit))
            return@channelFlow
        }
        send(Async.Loading)
        viewModelScope.launch(Dispatchers.Main) {
            //TODO: Maybe throw error if user is null???
            commitsRepository.getLastCommitForRepo(model, userStream.value!!)?.also { commit ->
                withContext(Dispatchers.Main) {
                    repoCommitMap[model] = commit
                }
                send(Async.Success(commit))
            } ?: run {
                send(Async.Error)
            }
        }.join()
    }
}