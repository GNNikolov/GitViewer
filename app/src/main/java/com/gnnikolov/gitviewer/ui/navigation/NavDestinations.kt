package com.gnnikolov.gitviewer.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavDestination {
    val route: String
}

@Serializable
data object UsersDestination : NavDestination {
    override val route: String
        get() = "users"
}

@Serializable
data class UserDetailsDestination(val userId: String) : NavDestination {
    override val route: String
        get() = "userDetails"
}