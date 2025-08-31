package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.concurrency.LockByKeyCache
import com.gnnikolov.gitviewer.data.local.dao.GitRepoDao
import com.gnnikolov.gitviewer.data.network.datasource.GitRepoNetworkDataSource
import com.gnnikolov.gitviewer.domain.IGitRepoRepository
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User
import com.gnnikolov.gitviewer.toDomain
import com.gnnikolov.gitviewer.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitRepoRepositoryImpl @Inject constructor(
    private val dataSource: GitRepoNetworkDataSource,
    private val dao: GitRepoDao,
    private val externalScope: CoroutineScope,
    private val cache: LockByKeyCache<String>
) : IGitRepoRepository {

    override suspend fun getReposForUser(user: User, refresh: Boolean): List<GitRepo>? {
        val state = cache.getState(user.id)
        return externalScope.async {
            state.runLocked(
                refresh,
                produce = {
                    val remoteData = dataSource.getReposForUser(user.name)
                    if (remoteData.isSuccess) {
                        remoteData.getOrNull()?.let {
                            dao.insertAll(*it.map { item -> item.toEntity(user.id) }.toTypedArray())
                        }
                    }
                    remoteData
                }, block = {
                    dao.getAllForUser(user.id).map { it.toDomain() }
                }
            )
        }.await()
    }

}