package com.gnnikolov.gitviewer.ui.userdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gnnikolov.gitviewer.R
import com.gnnikolov.gitviewer.domain.model.Commit
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.ui.common.shimmerEffect
import com.gnnikolov.gitviewer.ui.common.Async

//TODO: Check stability
@Composable
fun UserRepositoryItem(data: GitRepo) {
    val viewModel = viewModel<UserDetailsViewModel>()
    val commitState by produceState<Async<Commit?>>(Async.Loading, data, viewModel) {
        viewModel.lastCommitForRepo(data).collect { commitState ->
            value = commitState
        }
    }
    UserRepositoryItem(data, commitState)

}

//TODO!!!: Deferrer reading of commit state - investigate recompositions
//TODO: Preview
@Composable
private fun UserRepositoryItem(model: GitRepo, commitState: Async<Commit?>?) {
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = 10.dp
    ) {
        Column(
            Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            RepositoryContent(model)
            Divider(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp, bottom = 24.dp)
            )
            when (commitState) {
                null,
                Async.Loading -> LastCommitListItemLoading()

                is Async.Error -> {
                    //TODO: Show error Ui
                }

                is Async.Success<Commit?> -> {
                    val data = commitState.data
                    if (data != null) {
                        LastCommitListItemContent(commitState.data)
                    } else {
                        //TODO: Show commit not found
                    }
                }
            }
        }
    }
}


@Composable
private fun RepositoryContent(data: GitRepo) {
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
            text = commit.message,
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
                    commit.name,
                    commit.date
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