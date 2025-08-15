package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.repository.ICommitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommitsViewModel @Inject constructor(private val repository: ICommitsRepository) :
    ViewModel() {

    //TODO: Use Ui state
    private val data = mutableStateMapOf<GitRepoModel, Commit>()

    fun getLastCommit(model: GitRepoModel) = data[model]

    fun loadLastCommit(model: GitRepoModel) {
        if (data.containsKey(model))
            return
        viewModelScope.launch {
            val lastCommit = repository.getLastCommitForRepo(model)
            lastCommit?.let {
                data[model] = it
            }
        }
    }
}