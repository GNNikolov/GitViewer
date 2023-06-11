package com.gnnikolov.gitviewer.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gnnikolov.gitviewer.R

@Preview
@Composable
fun RepositoryListItem() {
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .wrapContentWidth()
    ) {
        Column(
            Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(Modifier.padding(top = 8.dp, start = 14.dp)) {
                Icon(
                    tint = Color.Yellow,
                    imageVector = Icons.Filled.Star,
                    modifier = Modifier
                        .size(12.dp)
                        .align(CenterVertically),
                    contentDescription = null
                )
                Text(
                    text = "18",
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
                text = "Watchers count: 30",
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
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_commit_24),
                    modifier = Modifier.padding(start = 16.dp),
                    contentDescription = null
                )
                Text(
                    text = "Last commit title",
                    modifier = Modifier
                        .align(CenterVertically)
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "7eb394dc7d489370de1e32bb91755a24bbd75627",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Author: go6o",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp),
                color = Color(0xFF8A8A8A),
                style = MaterialTheme.typography.subtitle2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "At: 2018-11-13 20:14:09",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                color = Color(0xFF8A8A8A),
                style = MaterialTheme.typography.subtitle2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}