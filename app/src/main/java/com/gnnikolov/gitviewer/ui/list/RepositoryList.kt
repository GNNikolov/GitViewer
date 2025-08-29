package com.gnnikolov.gitviewer.ui.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.ui.viewmodel.GitRepoViewModel

@Composable
fun RepositoryList() {
    val viewModel = viewModel<GitRepoViewModel>()
    val data by viewModel.data.collectAsStateWithLifecycle()
    RepositoryListContent(data)
}

//TODO: Fix stability!!!
@Composable
private fun RepositoryListContent(items: List<GitRepo>?) {
    if (items != null) {
        LazyColumn {
            itemsIndexed(items, { _, item -> item.id }) { _, item ->
                RepositoryListItem(item)
            }
        }
    } else {
        //TODO: Draw progress ui
    }
}