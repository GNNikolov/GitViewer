package com.gnnikolov.gitviewer.data.remote

import com.gnnikolov.gitviewer.data.remote.dto.CommitDTO
import com.gnnikolov.gitviewer.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GitRepoRemoteDataSource @Inject constructor(
    private val service: GitRepoService,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher
) {

    suspend fun getCommitsForRepo(name: String): Result<List<CommitDTO>> =
        withContext(ioDispatchers) {
            try {
                service.getCommitsForRepo(name).run {
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