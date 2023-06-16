package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.GitRepoModelsRepository
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class GitHubRepoModelsViewModel @Inject constructor(private val repository: GitRepoModelsRepository) :
    ViewModel() {

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