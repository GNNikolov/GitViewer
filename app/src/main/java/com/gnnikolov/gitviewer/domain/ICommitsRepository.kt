package com.gnnikolov.gitviewer.domain

import com.gnnikolov.gitviewer.domain.model.Commit
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User

interface ICommitsRepository {
    suspend fun getLastCommitForRepo(repo: GitRepo, user: User, refresh: Boolean = false): Commit?
}