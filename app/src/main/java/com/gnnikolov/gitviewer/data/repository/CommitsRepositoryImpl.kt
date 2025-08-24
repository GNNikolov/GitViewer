package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.concurrency.LockByKeyCache
import com.gnnikolov.gitviewer.data.database.CommitDao
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.remote.GitRepoRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommitsRepositoryImpl @Inject constructor(
    private val remoteDataSource: GitRepoRemoteDataSource,
    private val dao: CommitDao,
    private val externalScope: CoroutineScope,
    private val lockByKeyCache: LockByKeyCache<String>,
) : ICommitsRepository {

    override suspend fun getLastCommitForRepo(
        model: GitRepoModel,
        refresh: Boolean
    ): Commit? = withContext(Dispatchers.IO) {
        val state = lockByKeyCache.getState(model.id)
        externalScope.async {
            state.runLocked(
                refresh,
                produce = {
                    val result = remoteDataSource.getCommitsForRepo(model)
                    result.takeIf { it.isSuccess }?.getOrNull()?.let {
                        dao.insert(model, *it.toTypedArray())
                    }
                    result
                },
                block = {
                    dao.getLastCommitForRepo(model.id)
                }
            )
        }.await()
    }

}