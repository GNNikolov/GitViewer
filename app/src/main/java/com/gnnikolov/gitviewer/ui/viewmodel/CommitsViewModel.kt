package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.repository.CommitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommitsViewModel @Inject constructor(private val repository: CommitsRepository) :
    ViewModel() {

    private val repositoryCommitMap = mutableStateMapOf<GitRepoModel, Commit>()

    fun getLastCommitForRepo(repoModel: GitRepoModel) = repositoryCommitMap[repoModel]

    fun loadLatestCommitForRepo(data: GitRepoModel) {
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