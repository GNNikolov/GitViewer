package com.gnnikolov.gitviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.gnnikolov.gitviewer.ui.list.RepositoryList
import com.gnnikolov.gitviewer.ui.theme.GitViewerTheme
import com.gnnikolov.gitviewer.ui.viewmodel.CommitsViewModel
import com.gnnikolov.gitviewer.ui.viewmodel.GitHubRepoModelsViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: GitHubRepoModelsViewModel by viewModels { viewModelFactory }

    private val commitViewModel: CommitsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as GitViewerApplication).appComponent.inject(this)

        setContent {
            val data by viewModel.data.collectAsState()
            GitViewerTheme {
                data?.let { item ->
                    RepositoryList(item, commitViewModel.repositoryCommitMap) { model ->
                        commitViewModel.loadCommitsForRepo(model)
                    }
                }
            }
        }
    }
}
