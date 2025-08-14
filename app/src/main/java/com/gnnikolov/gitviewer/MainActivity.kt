package com.gnnikolov.gitviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gnnikolov.gitviewer.ui.list.RepositoryList
import com.gnnikolov.gitviewer.ui.theme.GitViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitViewerTheme { RepositoryList() }
        }
    }
}
