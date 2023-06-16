package com.gnnikolov.gitviewer.data

import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.remote.GitRepoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

//TODO: Impl caching
@Singleton
class GitRepoModelsRepository @Inject constructor(private val service: GitRepoService) {

    fun getGitRepos(): Flow<Result<List<GitRepoModel>>> = flow {
        val result = service.getGitRepos()
        val response = result.takeIf { it.isSuccessful }?.run {
            Result.success(body() ?: emptyList())
        } ?: result.run {
            Result.failure(Exception(errorBody()?.toString()))
        }
        emit(response)
    }.flowOn(Dispatchers.IO)

}