package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.domain.ICommitsRepository
import com.gnnikolov.gitviewer.domain.IGitRepoModelsRepository
import com.gnnikolov.gitviewer.domain.model.Commit
import com.gnnikolov.gitviewer.domain.model.GitRepo
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
    private val repository: IGitRepoModelsRepository,
    private val commitsRepository: ICommitsRepository
) : ViewModel() {

    private val _data = MutableStateFlow<List<GitRepo>?>(null)

    val data: StateFlow<List<GitRepo>?> = _data.asStateFlow()

    private val repoCommitMap = HashMap<GitRepo, Commit>()

    init {
        loadRemoteData()
    }

    private fun loadRemoteData() {
        viewModelScope.launch {
            val result = repository.getGitRepos()
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
            commitsRepository.getLastCommitForRepo(model)?.also { commit ->
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