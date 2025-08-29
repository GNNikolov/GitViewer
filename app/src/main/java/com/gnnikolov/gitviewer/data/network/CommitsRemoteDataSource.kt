package com.gnnikolov.gitviewer.data.network

import com.gnnikolov.gitviewer.data.local.entity.GitRepoEntity
import com.gnnikolov.gitviewer.data.local.entity.UserEntity
import com.gnnikolov.gitviewer.data.network.dto.CommitDTO
import com.gnnikolov.gitviewer.di.IoDispatcher
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommitsRemoteDataSource @Inject constructor(
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