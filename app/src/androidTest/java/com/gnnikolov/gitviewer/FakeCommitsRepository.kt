package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.domain.ICommitsRepository
import com.gnnikolov.gitviewer.domain.model.Commit
import com.gnnikolov.gitviewer.domain.model.GitRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCommitsRepository @Inject constructor() : ICommitsRepository {
    override suspend fun getLastCommitForRepo(model: GitRepo, refresh: Boolean): Commit? {
        TODO("Not yet implemented")
    }
}