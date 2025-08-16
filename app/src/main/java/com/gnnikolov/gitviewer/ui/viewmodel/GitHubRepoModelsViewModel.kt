package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.repository.ICommitsRepository
import com.gnnikolov.gitviewer.data.repository.IGitRepoModelsRepository
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

    private val _data = MutableStateFlow<List<GitRepoModel>?>(null)

    val data: StateFlow<List<GitRepoModel>?> = _data.asStateFlow()

    private val repoCommitMap = HashMap<GitRepoModel, Commit>()

    init {
        loadRemoteData()
    }

    private fun loadRemoteData() {
        viewModelScope.launch {
            val result = repository.getGitRepos()
            _data.emit(result)
        }
    }

    fun lastCommitForRepo(model: GitRepoModel): Flow<Async<Commit?>> = channelFlow {
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