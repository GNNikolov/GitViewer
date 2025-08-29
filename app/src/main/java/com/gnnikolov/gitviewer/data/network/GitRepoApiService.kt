package com.gnnikolov.gitviewer.data.network

import com.gnnikolov.gitviewer.data.network.dto.CommitDTO
import com.gnnikolov.gitviewer.data.network.dto.GitRepoDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitRepoApiService {

    @GET("/users/{user}/repos")
    suspend fun getReposForUser(@Path("user") user: String): Response<List<GitRepoDTO>>

    @GET("/repos/{user}/{repoName}/commits")
    suspend fun getCommitsForRepo(
        @Path("user") name: String,
        @Path("repoName") repoName: String
    ): Response<List<CommitDTO>>
}