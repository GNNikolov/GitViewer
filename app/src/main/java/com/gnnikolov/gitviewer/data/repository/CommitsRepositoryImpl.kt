package com.gnnikolov.gitviewer.data.repository

import androidx.annotation.GuardedBy
import com.gnnikolov.gitviewer.data.database.CommitDao
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.remote.GitRepoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommitsRepositoryImpl @Inject constructor(
    private val service: GitRepoService,
    private val dao: CommitDao,
    private val externalScope: CoroutineScope
) : ICommitsRepository {

    private val lock = Mutex()

    @GuardedBy("lock")
    private val cache = HashSet<Long>()

    override suspend fun getLastCommitForRepo(
        model: GitRepoModel,
        refresh: Boolean
    ): Commit? {
        //Somehow prevent concurrent remote data fetch for the same GitRepoModel id
        val shouldFetch = lock.withLock {
            if (refresh || !cache.contains(model.id)) {
                cache.add(model.id)
                true
            } else
                false
        }
        if (shouldFetch) {
            //TODO: Supporting emptying of cache if result is failed
            externalScope.async {
                withContext(Dispatchers.IO) {
                    getRemoteData(model).takeIf { it.isSuccess }?.getOrNull()?.let {
                        dao.insert(model, *it.toTypedArray())
                    }
                }
            }.await()
        }
        return withContext(Dispatchers.IO) { dao.getLastCommitForRepo(model.id) }
    }

    //TODO: In remote data source
    private suspend fun getRemoteData(repo: GitRepoModel): Result<List<Commit>> {
        return try {
            service.getCommitsForRepo(repo.name).run {
                if (isSuccessful)
                    Result.success(body() ?: emptyList())
                else
                    Result.failure(Exception())
            }
        } catch (t: Throwable) {
            Result.failure(Exception())
        }
    }

}