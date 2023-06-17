package com.gnnikolov.gitviewer.ui.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.repository.CommitsRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommitsViewModel @Inject constructor(private val repository: CommitsRepository) :
    ViewModel() {

    val repoCommitModelMap = mutableStateMapOf<GitRepoModel, Commit>()

    fun loadCommitsForRepo(data: GitRepoModel) {
        if (repoCommitModelMap.containsKey(data)) {
            return
        }
        viewModelScope.launch {
            repository.getCommitsForRepo(data).catch {
                //TODO: Handle exceptions
            }.collect {
                if (it.isSuccess) {
                    it.getOrNull()?.takeIf { it.isNotEmpty() }?.let { items ->
                        repoCommitModelMap[data] = items[0]
                    }
                }
            }
        }
    }
}