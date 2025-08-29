package com.gnnikolov.gitviewer.data.network

import com.gnnikolov.gitviewer.data.network.dto.GitRepoDTO
import com.gnnikolov.gitviewer.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GitRepoRemoteService @Inject constructor(
    private val apiService: GitRepoApiService,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher
) {
    suspend fun getReposForUser(user: String): Result<List<GitRepoDTO>> =
        withContext(ioDispatchers) {
            try {
                apiService.getReposForUser(user).run {
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