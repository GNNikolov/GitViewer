package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel

interface ICommitsRepository {
    suspend fun getLastCommitForRepo(gitRepoModel: GitRepoModel): Commit?
}