package com.gnnikolov.gitviewer.ui.userdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gnnikolov.gitviewer.ui.common.Async


@Composable
fun UserDetailsScreen(navigateBack: () -> Unit) {
    val uiState by hiltViewModel<UserDetailsViewModel>().uiStateStream.collectAsStateWithLifecycle()
    UserDetailsScreenContent(uiState, navigateBack)
}

@Composable
private fun UserDetailsScreenContent(uiState: UserDetailsUiState, navigateBack: () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(uiState.name) },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                )
            }
        ) {
            UserRepositories(uiState.reposState)
        }
    }
}

@Composable
private fun UserRepositories(reposState: Async<UserRepos>) {
    when (reposState) {
        Async.Error -> {
            TODO()
        }

        Async.Loading -> {
            //TODO: Placeholders
            Box(
                Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Async.Success<UserRepos> -> {
            LazyColumn {
                itemsIndexed(reposState.data.value, { _, item -> item.id }) { _, item ->
                    UserRepositoryItem(item)
                }
            }
        }
    }
}