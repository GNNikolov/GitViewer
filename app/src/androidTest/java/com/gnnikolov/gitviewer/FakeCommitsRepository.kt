package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.domain.ICommitsRepository
import com.gnnikolov.gitviewer.domain.model.Commit
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCommitsRepository @Inject constructor() : ICommitsRepository {
    override suspend fun getLastCommitForRepo(
        repo: GitRepo,
        user: User,
        refresh: Boolean
    ): Commit? {
        TODO("Not yet implemented")
    }
}