package com.gnnikolov.gitviewer.ui.userdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gnnikolov.gitviewer.ui.common.Async


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(navigateBack: () -> Unit) {
    val uiState by hiltViewModel<UserDetailsViewModel>().uiStateStream.collectAsStateWithLifecycle()
    UserDetailsScreenContent(uiState, navigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserDetailsScreenContent(uiState: UserDetailsUiState, navigateBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(uiState.name) },
                scrollBehavior = scrollBehavior,
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
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
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
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.background)
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
