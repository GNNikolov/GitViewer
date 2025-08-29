package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.concurrency.LockByKeyCache
import com.gnnikolov.gitviewer.data.local.dao.CommitDao
import com.gnnikolov.gitviewer.data.network.CommitsRemoteDataSource
import com.gnnikolov.gitviewer.domain.ICommitsRepository
import com.gnnikolov.gitviewer.domain.model.Commit
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User
import com.gnnikolov.gitviewer.mappers.toDomain
import com.gnnikolov.gitviewer.mappers.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommitsRepositoryImpl @Inject constructor(
    private val remoteDataSource: CommitsRemoteDataSource,
    private val dao: CommitDao,
    private val externalScope: CoroutineScope,
    private val lockByKeyCache: LockByKeyCache<String>,
) : ICommitsRepository {

    override suspend fun getLastCommitForRepo(
        repo: GitRepo,
        user: User,
        refresh: Boolean
    ): Commit? = withContext(Dispatchers.IO) {
        val state = lockByKeyCache.getState(repo.id)
        externalScope.async {
            state.runLocked(
                refresh,
                produce = {
                    val result = remoteDataSource.getCommitsForRepo(repo, user)
                    result.takeIf { it.isSuccess }?.getOrNull()?.let {
                        dao.insert(*it.map { item ->
                            item.toEntity(repo.id)
                        }.toTypedArray())
                    }
                    result
                },
                block = {
                    dao.getLastCommitForRepo(repo.id)?.toDomain()
                }
            )
        }.await()
    }

}