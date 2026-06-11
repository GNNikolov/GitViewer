package com.gnnikolov.gitviewer.ui.users

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.gnnikolov.gitviewer.R
import com.gnnikolov.gitviewer.domain.model.User
import com.gnnikolov.gitviewer.ui.common.Async
import com.gnnikolov.gitviewer.ui.common.theme.GitViewerTheme

@Composable
fun UsersScreen(onUserSelected: (User) -> Unit, onErrorDismissed: () -> Unit) {
    val viewModel = hiltViewModel<UsersViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UsersScreenContent(
        uiState = uiState,
        onClicked = onUserSelected,
        onTryAgain = viewModel::fetchUsers,
        onDismiss = onErrorDismissed,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onSearchActiveChange = viewModel::setSearchActive
    )
}

@Composable
private fun UsersScreenContent(
    uiState: UsersUiState,
    onClicked: (User) -> Unit,
    onTryAgain: () -> Unit,
    onDismiss: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchActiveChange: (Boolean) -> Unit
) {
    when (uiState.usersState) {
        Async.Error -> UsersError(onTryAgain, onDismiss)
        Async.Loading -> UsersLoading()
        is Async.Success -> UsersLoaded(
            users = uiState.resultUsers,
            searchQuery = uiState.searchQuery,
            isSearchActive = uiState.isSearchActive,
            onClicked = onClicked,
            onQueryChange = onSearchQueryChange,
            onVisibilityChanged = onSearchActiveChange
        )
    }
}

@Composable
private fun UsersError(onTryAgain: () -> Unit, onDismiss: () -> Unit) {
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

@Composable
private fun UsersLoading() {
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsersLoaded(
    users: Users,
    searchQuery: String,
    isSearchActive: Boolean,
    onClicked: (User) -> Unit,
    onQueryChange: (String) -> Unit,
    onVisibilityChanged: (Boolean) -> Unit
) {
    BackHandler(enabled = isSearchActive) {
        onVisibilityChanged(false)
    }

    Scaffold { paddingValues ->
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    //TODO: Calculate the padding dynamic based on real height
                    top = SearchBarDefaults.InputFieldHeight + 16.dp + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
            ) {
                items(users.value, key = { it.id }) {
                    UserListItem(it, onClicked)
                }
            }
            UsersSearchBar(
                modifier = Modifier.align(Alignment.TopCenter),
                query = searchQuery,
                isActive = isSearchActive,
                searchedUsers = users,
                onQueryChange = onQueryChange,
                onVisibilityChanged = onVisibilityChanged,
                onUserSelected = onClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsersSearchBar(
    query: String,
    isActive: Boolean,
    searchedUsers: Users,
    onQueryChange: (String) -> Unit,
    onVisibilityChanged: (Boolean) -> Unit,
    onUserSelected: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (!isActive) 16.dp else 0.dp,
                end = if (!isActive) 16.dp else 0.dp,
                top = if (!isActive) 8.dp else 0.dp
            ),
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { onVisibilityChanged(false) },
                expanded = isActive,
                onExpandedChange = onVisibilityChanged,
                placeholder = { Text(stringResource(R.string.search_user)) },
                leadingIcon = {
                    if (isActive) {
                        IconButton(onClick = { onVisibilityChanged(false) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                trailingIcon = if (isActive && query.isNotEmpty()) {
                    {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Filled.Close, contentDescription = null)
                        }
                    }
                } else null
            )
        },
        expanded = isActive,
        onExpandedChange = onVisibilityChanged
    ) {
        searchedUsers.value.forEach { user ->
            ListItem(
                headlineContent = { Text(user.name) },
                leadingContent = {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                },
                modifier = Modifier.clickable {
                    onUserSelected(user)
                    onVisibilityChanged(false)
                }
            )
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
            users = Users(
                listOf(
                    User("1", "go6o", ""),
                    User("2", "go6o", ""),
                    User("3", "go6o", ""),
                    User("4", "go6o", "")
                )
            ),
            searchQuery = "",
            isSearchActive = false,
            onClicked = {},
            onQueryChange = {},
            onVisibilityChanged = {}
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
