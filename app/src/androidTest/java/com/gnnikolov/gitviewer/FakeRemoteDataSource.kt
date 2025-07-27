package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.data.model.GitRepoModel

object FakeRemoteDataSource {

    fun getFakeRemoteData() = listOf(
        GitRepoModel(id = 0, stars = 1, watchers = 5, name = "Repository A"),
        GitRepoModel(id = 1, stars = 5, watchers = 5, name = "Repository B"),
        GitRepoModel(id = 2, stars = 7, watchers = 5, name = "Repository C")
    )

}