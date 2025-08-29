package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.data.local.dao.GitRepoDao
import com.gnnikolov.gitviewer.domain.IGitRepoRepository
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User
import com.gnnikolov.gitviewer.mappers.toDomain
import com.gnnikolov.gitviewer.mappers.toEntity
import javax.inject.Inject

class FakeGitRepoModelsRepository @Inject constructor(private val dao: GitRepoDao) :
    IGitRepoRepository {

    override suspend fun getReposForUser(user: User, refresh: Boolean): List<GitRepo> {
        dao.insertAll(*FakeRemoteDataSource.getFakeRemoteData().map {
                item -> item.toEntity()
        }.toTypedArray())
        return dao.getAllForUser(user.id).map { it.toDomain() }
    }

}