package com.gnnikolov.gitviewer.data.network.datasource

import com.gnnikolov.gitviewer.data.network.api.UserApiService
import com.gnnikolov.gitviewer.data.network.dto.UserDTO
import com.gnnikolov.gitviewer.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserNetworkDataSource @Inject constructor(
    private val apiService: UserApiService,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher
) {
    suspend fun getUsers(): Result<List<UserDTO>> = withContext(ioDispatchers) {
        try {
            val result = apiService.getUsers()
            if (result.isSuccessful)
                Result.success(result.body() ?: emptyList())
            else
                Result.failure(Exception())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}