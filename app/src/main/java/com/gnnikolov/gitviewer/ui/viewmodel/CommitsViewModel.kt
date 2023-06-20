package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.repository.CommitsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommitsViewModel @Inject constructor(private val repository: CommitsRepository) :
    ViewModel() {

    val repositoryCommitMap = mutableStateMapOf<GitRepoModel, Commit>()

    fun loadCommitsForRepo(data: GitRepoModel) {
        if (repositoryCommitMap.containsKey(data)) {
            return
        }
        viewModelScope.launch {
            val lastCommit = repository.getLastCommitForRepo(data)
            lastCommit?.let {
                repositoryCommitMap[data] = it
            }
        }
    }
}