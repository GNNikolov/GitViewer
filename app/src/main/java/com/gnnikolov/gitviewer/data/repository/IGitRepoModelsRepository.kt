package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.data.model.GitRepoModel

interface IGitRepoModelsRepository {
    suspend fun getGitRepos(): List<GitRepoModel>
}