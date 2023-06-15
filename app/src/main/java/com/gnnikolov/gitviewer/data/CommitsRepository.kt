package com.gnnikolov.gitviewer.data

import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.remote.GitRepoService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//TODO: Make injectable via DI
//TODO: Impl caching
class CommitsRepository(private val service: GitRepoService) {

    fun getCommitsForRepo(gitRepoModel: GitRepoModel): Flow<Result<List<Commit>>> = flow {
        val result = service.getCommitsForRepo(gitRepoModel.name)
        val response = result.takeIf { it.isSuccessful }?.run {
            Result.success(body() ?: emptyList())
        } ?: result.run {
            Result.failure(Exception(errorBody()?.toString()))
        }
        emit(response)
    }

    companion object {
        private var INSTANCE: CommitsRepository? = null

        fun getInstance(service: GitRepoService): CommitsRepository =
            INSTANCE ?: CommitsRepository(service).also {
                INSTANCE = it
            }
    }

}