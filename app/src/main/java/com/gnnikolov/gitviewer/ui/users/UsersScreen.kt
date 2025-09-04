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
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    //TODO? MVI?
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
                .background(MaterialTheme.colors.background)
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
                        Text(stringResource(R.string.try_again)) // Often uppercase in M2 dialogs
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text(stringResource(R.string.close)) // Often uppercase in M2 dialogs
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
                .background(MaterialTheme.colors.background)
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
                .background(MaterialTheme.colors.background)
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

@Composable
private fun Content(content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.github_users)) })
        }
    ) {
        content()
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
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //TODO: Implement dark theme and use material colors!!!
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
                style = MaterialTheme.typography.body1,
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
        modifier = Modifier.background(Color.Black.copy(alpha = ContentAlpha.disabled)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            tint = Color.Black.copy(alpha = ContentAlpha.medium),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            imageVector = Icons.Default.Person,
            contentDescription = ""
        )
    }
}