package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.data.model.Commit
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
class CommitsRepository @Inject constructor(private val service: GitRepoService) {

    fun getCommitsForRepo(gitRepoModel: GitRepoModel): Flow<Result<List<Commit>>> = flow {
        val result = service.getCommitsForRepo(gitRepoModel.name)
        val response = result.takeIf { it.isSuccessful }?.run {
            Result.success(body() ?: emptyList())
        } ?: result.run {
            Result.failure(Exception(errorBody()?.toString()))
        }
        emit(response)
    }.flowOn(Dispatchers.IO)


}