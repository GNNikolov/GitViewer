package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.data.database.GitRepoModelDao
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.repository.IGitRepoModelsRepository
import javax.inject.Inject

class FakeGitRepoModelsRepository @Inject constructor(private val dao: GitRepoModelDao) :
    IGitRepoModelsRepository {

    private val fakeRemoteData = listOf(
        GitRepoModel(id = 0, stars = 1, watchers = 5, name = "Repository A"),
        GitRepoModel(id = 1, stars = 5, watchers = 5, name = "Repository B"),
        GitRepoModel(id = 2, stars = 7, watchers = 5, name = "Repository C")
    )

    override suspend fun getGitRepos(): List<GitRepoModel> {
        dao.insertAll(*fakeRemoteData.toTypedArray())
        return dao.getAll()
    }

}