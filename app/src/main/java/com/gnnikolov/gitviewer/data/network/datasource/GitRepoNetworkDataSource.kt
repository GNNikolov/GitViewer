package com.gnnikolov.gitviewer.data.network.datasource

import com.gnnikolov.gitviewer.data.network.api.GitRepoApiService
import com.gnnikolov.gitviewer.data.network.dto.GitRepoDTO
import com.gnnikolov.gitviewer.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GitRepoNetworkDataSource @Inject constructor(
    private val apiService: GitRepoApiService,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher
) {
    suspend fun getReposForUser(userName: String): Result<List<GitRepoDTO>> =
        withContext(ioDispatchers) {
            try {
                val result = apiService.getReposForUser(userName)
                if (result.isSuccessful)
                    Result.success(result.body() ?: emptyList())
                else
                    Result.failure(Exception())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}