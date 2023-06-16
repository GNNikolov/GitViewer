package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.CommitsRepository
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.remote.GitHubApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CommitsViewModel : ViewModel() {

    //FIXME: Inject the repo to constructor with DI!!!
    private val repository by lazy { CommitsRepository.getInstance(GitHubApi.gitRepoService) }

    val repoCommitModelMap = mutableStateMapOf<GitRepoModel, Commit>()

    fun loadCommitsForRepo(data: GitRepoModel) {
        if (repoCommitModelMap.containsKey(data)) {
            return
        }
        viewModelScope.launch {
            repository.getCommitsForRepo(data).collect {
                if (it.isSuccess) {
                    it.getOrNull()?.takeIf { it.isNotEmpty() }?.let { items ->
                        repoCommitModelMap[data] = items[0]
                    }
                }
            }
        }
    }
}