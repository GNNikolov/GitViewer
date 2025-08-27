package com.gnnikolov.gitviewer.domain

import com.gnnikolov.gitviewer.domain.model.GitRepo

interface IGitRepoModelsRepository {
    suspend fun getGitRepos(): List<GitRepo>
}