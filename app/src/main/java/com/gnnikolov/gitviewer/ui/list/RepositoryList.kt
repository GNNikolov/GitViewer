package com.gnnikolov.gitviewer.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gnnikolov.gitviewer.R
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.ui.shimmerEffect

@Composable
fun RepositoryList(
    items: List<GitRepoModel>,
    repoModelCommitMap: SnapshotStateMap<GitRepoModel, Commit>,
    loadCommitData: (GitRepoModel) -> Unit
) {
    LazyColumn {
        itemsIndexed(items, null) { _, item ->
            RepositoryListItem(item, repoModelCommitMap[item], loadCommitData)
        }
    }
}

@Composable
private fun RepositoryListItem(
    data: GitRepoModel,
    commit: Commit?,
    loadCommitData: (GitRepoModel) -> Unit
) {
    LaunchedEffect(key1 = data, block = {
        loadCommitData(data)
    })
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 10.dp
    ) {
        Column(
            Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            RepositoryContent(data)
            Divider(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp, bottom = 24.dp)
            )
            if (commit != null) {
                LastCommitListItemContent(commit)
            } else {
                LastCommitListItemLoading()
            }
        }
    }
}

@Composable
private fun RepositoryContent(data: GitRepoModel) {
    Column {
        Row(Modifier.padding(top = 8.dp, start = 16.dp)) {
            Icon(
                tint = MaterialTheme.colors.primary,
                imageVector = Icons.Filled.Star,
                modifier = Modifier.align(CenterVertically),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.repo_starts, data.stars),
                modifier = Modifier
                    .align(CenterVertically),
                style = MaterialTheme.typography.overline,
                fontWeight = FontWeight.SemiBold
            )
        }
        Text(
            text = data.name,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.h5,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = stringResource(R.string.repo_watchers, data.watchers),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp),
            color = Color(0xFF8A8A8A),
            style = MaterialTheme.typography.subtitle2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun LastCommitListItemLoading() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .padding(horizontal = 16.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .padding(horizontal = 16.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .padding(horizontal = 16.dp)
                .shimmerEffect(),
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LastCommitListItemContent(commit: Commit) {
    Column {
        Text(
            text = commit.details.message,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.subtitle1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = stringResource(
                id = R.string.commit_author_date,
                formatArgs = arrayOf(
                    commit.details.committer.name,
                    commit.details.committer.date
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            color = Color(0xFF8A8A8A),
            style = MaterialTheme.typography.subtitle2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = stringResource(id = R.string.commit_id, commit.sha),
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp)
                .padding(bottom = 16.dp),
            color = Color(0xFF8A8A8A),
            style = MaterialTheme.typography.subtitle2,
            maxLines = 2
        )
    }
}