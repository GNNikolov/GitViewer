package com.gnnikolov.gitviewer.domain

import com.gnnikolov.gitviewer.domain.model.Commit
import com.gnnikolov.gitviewer.domain.model.GitRepo

interface ICommitsRepository {
    suspend fun getLastCommitForRepo(model: GitRepo, refresh: Boolean = false): Commit?
}