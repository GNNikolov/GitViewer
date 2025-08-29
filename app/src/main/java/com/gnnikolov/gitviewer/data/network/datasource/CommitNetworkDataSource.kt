package com.gnnikolov.gitviewer.data.network.datasource

import com.gnnikolov.gitviewer.data.network.api.GitRepoApiService
import com.gnnikolov.gitviewer.data.network.dto.CommitDTO
import com.gnnikolov.gitviewer.di.IoDispatcher
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommitNetworkDataSource @Inject constructor(
    private val service: GitRepoApiService,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher
) {

    suspend fun getCommitsForRepo(repo: GitRepo, user: User): Result<List<CommitDTO>> =
        withContext(ioDispatchers) {
            try {
                service.getCommitsForRepo(user.name, repo.name).run {
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