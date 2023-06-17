package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.data.database.GitRepoModelDao
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.remote.GitRepoService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitRepoModelsRepository @Inject constructor(
    private val service: GitRepoService,
    private val dao: GitRepoModelDao,
) {

    fun getGitRepos(): Flow<List<GitRepoModel>?> = channelFlow {
        val loadRemote = async(Dispatchers.IO) { getRemoteData() }
        val loadLocal = async(Dispatchers.IO) { dao.getAll() }
        val result = syncData(loadRemote.await(), loadLocal.await())
        send(result)
    }.flowOn(Dispatchers.IO)

    private suspend fun getRemoteData(): Result<List<GitRepoModel>> {
        return try {
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

    private suspend fun syncData(
        remote: Result<List<GitRepoModel>>,
        local: List<GitRepoModel>
    ) = remote.getOrNull()?.takeIf { remote.isSuccess }?.run {
        dao.deleteAll()
        dao.insertAll(*this.toTypedArray())
        dao.getAll()
    } ?: local.takeIf { it.isNotEmpty() }

}