package com.gnnikolov.gitviewer.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gnnikolov.gitviewer.ui.userdetails.UserDetailsScreen
import com.gnnikolov.gitviewer.ui.users.UsersScreen


@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    NavHost(
        navController,
        startDestination = UsersDestination
    ) {
        composable<UsersDestination> {
            UsersScreen(
                onUserSelected = {
                    navController.navigate(UserDetailsDestination(it.id))
                },
                onErrorDismissed = {
                    //TODO: Find more elegant way to handle this
                    (context as? Activity)?.finishAndRemoveTask()
                }
            )
        }
        composable<UserDetailsDestination> {
            UserDetailsScreen { navController.popBackStack() }
        }
    }
}