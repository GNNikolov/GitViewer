package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.data.local.dao.GitRepoDao
import com.gnnikolov.gitviewer.data.remote.GitRepoService
import com.gnnikolov.gitviewer.domain.IGitRepoModelsRepository
import com.gnnikolov.gitviewer.mappers.toDomain
import com.gnnikolov.gitviewer.mappers.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitRepoModelsRepositoryImpl @Inject constructor(
    private val service: GitRepoService,
    private val dao: GitRepoDao,
) : IGitRepoModelsRepository {

    override suspend fun getGitRepos() = withContext(Dispatchers.IO) {
        getRemoteData().takeIf { it.isSuccess }?.getOrNull()?.let { data ->
            dao.insertAll(*data.map { it.toEntity() }.toTypedArray())
        }
        dao.getAll().map { it.toDomain() }
    }

    private suspend fun getRemoteData() = try {
        service.getGitRepos().run {
            if (isSuccessful)
                Result.success(body() ?: emptyList())
            else
                Result.failure(Exception())
        }
    } catch (t: Throwable) {
        Result.failure(Exception())
    }

}