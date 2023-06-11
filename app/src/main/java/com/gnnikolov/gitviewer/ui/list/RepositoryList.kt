package com.gnnikolov.gitviewer.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gnnikolov.gitviewer.ui.ShimmerLoadingUi
import com.gnnikolov.gitviewer.ui.shimmerEffect

@Composable
fun RepositoryList() {
    LazyColumn {
        items(10, null) {
            RepositoryListItem()
        }
    }
}

@Preview
@Composable
private fun RepositoryListItem() {
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(Modifier.padding(top = 8.dp, start = 16.dp)) {
                Icon(
                    tint = Color.Yellow,
                    imageVector = Icons.Filled.Star,
                    modifier = Modifier.align(CenterVertically),
                    contentDescription = null
                )
                Text(
                    text = "18(stars)",
                    modifier = Modifier
                        .align(CenterVertically),
                    style = MaterialTheme.typography.overline,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = "Repo name",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.h5,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "30 Watchers",
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
            Divider(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp, bottom = 24.dp)
            )
            ShimmerLoadingUi(
                isLoading = true,
                content = { LastCommitListItemContent() },
                loadingContent = { LastCommitListItemLoading() })
        }
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
private fun LastCommitListItemContent() {
    Column {
        Text(
            text = "Merge pull request #599 from android/jakew/closing-time/2018-07-24\\n\\nDirect contributors to AOSP",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.subtitle1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Change by GitUser at 2018-11-13 20:14:09",
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
            text = "Id: 7eb394dc7d489370de1e32bb91755a24bbd75627",
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