package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User

object FakeRemoteDataSource {

    val fakeUser by lazy {
        User(
            id = "MDQ6VXNlcjE=",
            name = "go6o",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4"
        )
    }

    fun getFakeRemoteData() = listOf(
        GitRepo(id = "0", stars = 1, watchers = 5, name = "Repository A", userId = fakeUser.id),
        GitRepo(id = "1", stars = 5, watchers = 5, name = "Repository B", userId = fakeUser.id),
        GitRepo(id = "2", stars = 7, watchers = 5, name = "Repository C", userId = fakeUser.id)
    )

}