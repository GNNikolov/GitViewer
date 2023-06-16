package com.gnnikolov.gitviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.gnnikolov.gitviewer.ui.list.RepositoryList
import com.gnnikolov.gitviewer.ui.theme.GitViewerTheme
import com.gnnikolov.gitviewer.ui.viewmodel.CommitsViewModel
import com.gnnikolov.gitviewer.ui.viewmodel.GitHubRepoModelsViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GitHubRepoModelsViewModel by viewModels()

    private val commitViewModel: CommitsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val data by viewModel.data.collectAsState()
            GitViewerTheme {
                data.getOrNull()?.let { item ->
                    RepositoryList(item, commitViewModel.repoCommitModelMap) { model ->
                        commitViewModel.loadCommitsForRepo(model)
                    }
                }
            }
        }
    }
}
