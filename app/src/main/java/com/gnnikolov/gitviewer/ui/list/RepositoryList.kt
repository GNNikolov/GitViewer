package com.gnnikolov.gitviewer.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
                    fontWeight = FontWeight.Bold
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
                text = "Languages: Java, C#",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
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