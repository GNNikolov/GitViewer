package com.gnnikolov.gitviewer.data

import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.remote.GitRepoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

//TODO: Make injectable via DI
class GitRepoModelsRepository private constructor(private val service: GitRepoService) {

    fun getGitRepos(): Flow<Result<List<GitRepoModel>>> = flow {
        val result = service.getGitRepos()
        val response = result.takeIf { it.isSuccessful }?.run {
            Result.success(body() ?: emptyList())
        } ?: result.run {
            Result.failure(Exception(errorBody()?.toString()))
        }
        emit(response)
    }.flowOn(Dispatchers.IO)

    companion object {
        private var INSTANCE: GitRepoModelsRepository? = null

        fun getInstance(service: GitRepoService): GitRepoModelsRepository =
            INSTANCE ?: GitRepoModelsRepository(service).also {
                INSTANCE = it
            }
    }
}