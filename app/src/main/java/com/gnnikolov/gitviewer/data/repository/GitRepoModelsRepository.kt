package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.data.database.GitRepoModelDao
import com.gnnikolov.gitviewer.data.remote.GitRepoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitRepoModelsRepository @Inject constructor(
    private val service: GitRepoService,
    private val dao: GitRepoModelDao,
) {

    suspend fun getGitRepos() = withContext(Dispatchers.IO) {
        getRemoteData().takeIf { it.isSuccess }?.getOrNull()?.let {
            dao.insertAll(*it.toTypedArray())
        }
        dao.getAll()
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