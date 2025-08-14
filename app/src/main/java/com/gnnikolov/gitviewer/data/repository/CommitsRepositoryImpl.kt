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
    private val cache = HashMap<GitRepoModel, Commit>()

    override suspend fun getLastCommitForRepo(
        gitRepoModel: GitRepoModel,
        refresh: Boolean
    ): Commit? {
        if (refresh || lock.withLock { cache[gitRepoModel] == null }) {
            externalScope.async {
                withContext(Dispatchers.IO) {
                    getRemoteData(gitRepoModel).takeIf { it.isSuccess }?.getOrNull()?.let {
                        dao.insert(gitRepoModel, *it.toTypedArray())
                    }
                    val result = dao.getLastCommitForRepo(gitRepoModel.id)
                    if (result != null) {
                        lock.withLock {
                            cache[gitRepoModel] = result
                        }
                    }
                }
            }.await()
        }
        return lock.withLock { cache[gitRepoModel] }
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