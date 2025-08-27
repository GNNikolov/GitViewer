package com.gnnikolov.gitviewer.data.remote

import com.gnnikolov.gitviewer.data.remote.dto.CommitDTO
import com.gnnikolov.gitviewer.data.remote.dto.GitRepoDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitRepoService {
    @GET("/users/android/repos")
    suspend fun getGitRepos(): Response<List<GitRepoDTO>>

    @GET("/repos/android/{name}/commits")
    suspend fun getCommitsForRepo(@Path("name") name: String): Response<List<CommitDTO>>
}