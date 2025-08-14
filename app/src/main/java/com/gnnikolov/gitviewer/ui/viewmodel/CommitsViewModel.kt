package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.repository.ICommitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommitsViewModel @Inject constructor(private val repository: ICommitsRepository) :
    ViewModel() {

    //TODO: Use Ui state
    private val _state = MutableStateFlow<Commit?>(null)

    val state = _state.asStateFlow()

    fun loadLatestCommitForRepo(data: GitRepoModel) {
        viewModelScope.launch {
            val value = repository.getLastCommitForRepo(data)
            _state.value = value
        }
    }
}