package com.gnnikolov.gitviewer.ui.test

import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User

object Utils {

    val fakeUsers = listOf(
        User(
            id = "MDQ6VXNlcjE=",
            name = "Go6o",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4"
        )
    )

    fun getFakeGitRepos(userId: String) = listOf(
        GitRepo(
            id = "repo1",
            name = "Repo One",
            stars = 10,
            watchers = 5,
            userId = userId,
        ),
        GitRepo(
            id = "repo2",
            name = "Repo Two",
            stars = 20,
            watchers = 10,
            userId = userId,
        )
    )
}