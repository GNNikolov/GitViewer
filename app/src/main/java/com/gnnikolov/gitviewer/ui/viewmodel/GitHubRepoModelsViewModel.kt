package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.repository.GitRepoModelsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GitHubRepoModelsViewModel @Inject constructor(private val repository: GitRepoModelsRepository) :
    ViewModel() {

    private val _data = MutableStateFlow<List<GitRepoModel>?>(null)
    val data: StateFlow<List<GitRepoModel>?> = _data.asStateFlow()

    init {
        loadRemoteData()
    }

    private fun loadRemoteData() {
        viewModelScope.launch {
            val result = repository.getGitRepos()
            _data.emit(result)
        }
    }
}