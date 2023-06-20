package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.data.database.CommitDao
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.remote.GitRepoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommitsRepository @Inject constructor(
    private val service: GitRepoService,
    private val dao: CommitDao
) {

    suspend fun getLastCommitForRepo(gitRepoModel: GitRepoModel) = withContext(Dispatchers.IO) {
        getRemoteData(gitRepoModel).takeIf { it.isSuccess }?.getOrNull()?.let {
            dao.insert(gitRepoModel, *it.toTypedArray())
        }
        return@withContext dao.getLastCommitForRepo(gitRepoModel.id)
    }

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