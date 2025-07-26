package com.gnnikolov.gitviewer

import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel
import com.gnnikolov.gitviewer.data.repository.ICommitsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCommitsRepository @Inject constructor() : ICommitsRepository {
    override suspend fun getLastCommitForRepo(gitRepoModel: GitRepoModel): Commit? {
        TODO("Not yet implemented")
    }
}