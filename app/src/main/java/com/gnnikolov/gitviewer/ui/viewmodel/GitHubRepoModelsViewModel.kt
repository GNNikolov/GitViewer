package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.local.dao.UserDao
import com.gnnikolov.gitviewer.domain.ICommitsRepository
import com.gnnikolov.gitviewer.domain.IGitRepoRepository
import com.gnnikolov.gitviewer.domain.model.Commit
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User
import com.gnnikolov.gitviewer.mappers.toEntity
import com.gnnikolov.gitviewer.ui.state.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GitHubRepoModelsViewModel @Inject constructor(
    private val repository: IGitRepoRepository,
    private val commitsRepository: ICommitsRepository,
    private val dao: UserDao
) : ViewModel() {

    //FIXME!!!: DELETE this, temp hardcoded user for testing only
    private val local = User(
        id = "MDQ6VXNlcjE=",
        name = "mojombo",
        avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4"
    )

    private val _data = MutableStateFlow<List<GitRepo>?>(null)

    val data: StateFlow<List<GitRepo>?> = _data.asStateFlow()

    private val repoCommitMap = HashMap<GitRepo, Commit>()

    init {
        loadRemoteData()
    }

    private fun loadRemoteData() {
        viewModelScope.launch {
            dao.insert(local.toEntity())
            val result = repository.getReposForUser(local)
            _data.emit(result)
        }
    }

    fun lastCommitForRepo(model: GitRepo): Flow<Async<Commit?>> = channelFlow {
        repoCommitMap[model]?.let { commit ->
            send(Async.Success(commit))
            return@channelFlow
        }
        send(Async.Loading)
        viewModelScope.launch(Dispatchers.Main) {
            commitsRepository.getLastCommitForRepo(model, local)?.also { commit ->
                withContext(Dispatchers.Main) {
                    repoCommitMap[model] = commit
                }
                send(Async.Success(commit))
            } ?: run {
                send(Async.Error(-1))
            }
        }.join()
    }
}