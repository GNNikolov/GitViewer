package com.gnnikolov.gitviewer.data.remote

import com.gnnikolov.gitviewer.data.model.GitRepoModel
import retrofit2.Response
import retrofit2.http.GET

interface GitRepoService {
    @GET("/users/android/repos")
    suspend fun getGitRepos(): Response<List<GitRepoModel>>
}