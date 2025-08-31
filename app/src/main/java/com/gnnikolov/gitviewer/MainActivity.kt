package com.gnnikolov.gitviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gnnikolov.gitviewer.ui.navigation.NavGraph
import com.gnnikolov.gitviewer.ui.common.theme.GitViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitViewerTheme { NavGraph() }
        }
    }
}
