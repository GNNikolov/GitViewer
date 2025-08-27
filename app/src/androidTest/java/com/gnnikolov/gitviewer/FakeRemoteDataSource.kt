package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.domain.model.GitRepo

object FakeRemoteDataSource {

    fun getFakeRemoteData() = listOf(
        GitRepo(id = "0", stars = 1, watchers = 5, name = "Repository A"),
        GitRepo(id = "1", stars = 5, watchers = 5, name = "Repository B"),
        GitRepo(id = "2", stars = 7, watchers = 5, name = "Repository C")
    )

}