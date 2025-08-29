package com.gnnikolov.gitviewer.domain

import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User

interface IGitRepoRepository {
    suspend fun getReposForUser(user: User, refresh: Boolean = false): List<GitRepo>?
}