package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.repository.GitRepoModelsRepository
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class GitHubRepoModelsViewModel @Inject constructor(private val repository: GitRepoModelsRepository) :
    ViewModel() {

    //TODO: Add UI state
    private val _data = MutableStateFlow<List<GitRepoModel>?>(null)
    val data: StateFlow<List<GitRepoModel>?> = _data.asStateFlow()

    init {
        loadRemoteData()
    }

    private fun loadRemoteData() {
        viewModelScope.launch {
            repository.getGitRepos().collect {
                _data.emit(it)
            }
        }
    }
}