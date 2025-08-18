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

    private class SyncState {
        private val mutex = Mutex()

        @GuardedBy("mutex")
        private var isSynced: Boolean = false

        suspend inline fun <T> synchronize(
            reset: Boolean,
            action: (isSynced: Boolean) -> T //TODO: Return Result?
        ): T {
            return mutex.withLock {
                if (reset)
                    isSynced = false
                //TODO: Set isSynced = true if action is successful?
                val result = action(isSynced)
                if (!isSynced)
                    isSynced = true
                result
            }
        }
    }

    private val cacheLock = Mutex()

    //TODO: Supporting emptying of cache!!!
    @GuardedBy("cacheLock")
    private val idToStateCache = HashMap<Long, SyncState>()

    override suspend fun getLastCommitForRepo(
        model: GitRepoModel,
        refresh: Boolean
    ): Commit? = withContext(Dispatchers.IO) {
        val state = cacheLock.withLock { idToStateCache.getOrPut(model.id) { SyncState() } }
        externalScope.async {
            state.synchronize(refresh) { isSync ->
                if (!isSync) {
                    getRemoteData(model).takeIf { it.isSuccess }?.getOrNull()?.let {
                        dao.insert(model, *it.toTypedArray())
                    }
                }
                dao.getLastCommitForRepo(model.id)
            }
        }.await()
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