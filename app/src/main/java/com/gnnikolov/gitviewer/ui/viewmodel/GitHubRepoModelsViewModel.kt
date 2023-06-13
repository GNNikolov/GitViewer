package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.GitRepoModelsRepository
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.remote.GitHubApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GitHubRepoModelsViewModel : ViewModel() {
    //FIXME: Inject the repo to constructor with DI!!!
    private val repository by lazy { GitRepoModelsRepository.getInstance(GitHubApi.gitRepoService) }

    //TODO: Add loading state
    private val _data = MutableStateFlow<Result<List<GitRepoModel>>>(Result.success(emptyList()))
    val data: StateFlow<Result<List<GitRepoModel>>> = _data.asStateFlow()

    init {
        loadRemoteData()
    }

    private fun loadRemoteData() {
        viewModelScope.launch {
            repository.getGitRepos().catch {
                _data.emit(Result.failure(it))
            }.collect {
                _data.emit(it)
            }
        }
    }
}