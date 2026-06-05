package com.gnnikolov.gitviewer.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.gnnikolov.gitviewer.R
import com.gnnikolov.gitviewer.domain.model.User
import com.gnnikolov.gitviewer.ui.common.Async
import com.gnnikolov.gitviewer.ui.common.theme.GitViewerTheme

@Composable
fun UsersScreen(onUserSelected: (User) -> Unit, onErrorDismissed: () -> Unit) {
    val viewModel = hiltViewModel<UsersViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    UsersScreenContent(
        state = state,
        onClicked = onUserSelected,
        onTryAgain = viewModel::fetchUsers,
        onDismiss = onErrorDismissed
    )
}

@Composable
private fun UsersScreenContent(
    state: Async<Users>,
    onClicked: (User) -> Unit,
    onTryAgain: () -> Unit,
    onDismiss: () -> Unit
) {
    when (state) {
        Async.Error -> UsersError(onTryAgain, onDismiss)
        Async.Loading -> UsersLoading()
        is Async.Success<Users> -> UsersLoaded(state.data, onClicked)
    }
}

@Composable
private fun UsersError(onTryAgain: () -> Unit, onDismiss: () -> Unit) {
    Content {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(stringResource(R.string.error_fetching_users))
                },
                confirmButton = {
                    TextButton(onClick = onTryAgain) {
                        Text(stringResource(R.string.try_again))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.close))
                    }
                }
            )
        }
    }
}

@Composable
private fun UsersLoading() {
    Content {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun UsersLoaded(data: Users, onClicked: (User) -> Unit) {
    Content {
        LazyVerticalGrid(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(
                16.dp,
                Alignment.CenterHorizontally
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(all = 16.dp)
        ) {
            items(data.value, key = { it -> it.id }) {
                UserListItem(it, onClicked)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(content: @Composable () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.github_users)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            content()
        }
    }
}

@Composable
@Preview(widthDp = 390, heightDp = 830)
private fun UsersErrorPreview() {
    GitViewerTheme {
        UsersError({}) { }
    }
}

@Composable
@Preview(widthDp = 390, heightDp = 830)
private fun UsersLoadingPreview() {
    GitViewerTheme {
        UsersLoading()
    }
}

@Composable
@Preview(widthDp = 390, heightDp = 830)
private fun UserListItemPreview() {
    GitViewerTheme {
        UsersLoaded(
            Users(
                arrayListOf(
                    User("1", "go6o", ""),
                    User("2", "go6o", ""),
                    User("3", "go6o", ""),
                    User("4", "go6o", "")
                )
            ), {}
        )
    }
}

@Composable
private fun UserListItem(data: User, onClicked: (User) -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onClicked(data) }
            .fillMaxSize()
            .aspectRatio(1f),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubcomposeAsyncImage(
                model = data.avatarUrl,
                error = { UserIconPlaceHolder() },
                loading = { UserIconPlaceHolder() },
                contentDescription = data.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 6.dp,
                            topEnd = 6.dp
                        )
                    )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun UserIconPlaceHolder() {
    Box(
        modifier = Modifier.background(Color.Black.copy(alpha = 0.38f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            tint = Color.Black.copy(alpha = 0.74f),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            imageVector = Icons.Default.Person,
            contentDescription = ""
        )
    }
}
