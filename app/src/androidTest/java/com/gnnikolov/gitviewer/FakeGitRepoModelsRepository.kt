package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.data.database.GitRepoModelDao
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.repository.IGitRepoModelsRepository
import javax.inject.Inject

class FakeGitRepoModelsRepository @Inject constructor(private val dao: GitRepoModelDao) :
    IGitRepoModelsRepository {

    override suspend fun getGitRepos(): List<GitRepoModel> {
        dao.insertAll(*FakeRemoteDataSource.getFakeRemoteData().toTypedArray())
        return dao.getAll()
    }

}