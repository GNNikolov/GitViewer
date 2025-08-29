package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.data.local.dao.GitRepoDao
import com.gnnikolov.gitviewer.domain.IGitRepoModelsRepository
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.mappers.toDomain
import com.gnnikolov.gitviewer.mappers.toEntity
import javax.inject.Inject

class FakeGitRepoModelsRepository @Inject constructor(private val dao: GitRepoDao) :
    IGitRepoModelsRepository {

    override suspend fun getGitRepos(): List<GitRepo> {
        dao.insertAll(*FakeRemoteDataSource.getFakeRemoteData().map {
            item -> item.toEntity()
        }.toTypedArray())
        return dao.getAll().map { it.toDomain() }
    }

}