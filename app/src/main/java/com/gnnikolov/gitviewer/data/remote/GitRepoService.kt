package com.gnnikolov.gitviewer.data.remote

import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitRepoService {
    @GET("/users/android/repos")
    suspend fun getGitRepos(): Response<List<GitRepoModel>>

    @GET("/repos/android/{name}/commits")
    suspend fun getCommitsForRepo(@Path("name") name: String): Response<List<Commit>>
}