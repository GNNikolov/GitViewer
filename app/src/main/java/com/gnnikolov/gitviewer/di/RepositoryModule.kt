package com.gnnikolov.gitviewer.di

import com.gnnikolov.gitviewer.data.database.CommitDao
import com.gnnikolov.gitviewer.data.database.GitRepoModelDao
import com.gnnikolov.gitviewer.data.remote.GitRepoService
import com.gnnikolov.gitviewer.data.repository.CommitsRepository
import com.gnnikolov.gitviewer.data.repository.GitRepoModelsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideGitRepoModelRepository(
        service: GitRepoService,
        dao: GitRepoModelDao
    ): GitRepoModelsRepository {
        return GitRepoModelsRepository(service, dao)
    }

    @Provides
    @Singleton
    fun provideCommitsRepository(service: GitRepoService, dao: CommitDao): CommitsRepository {
        return CommitsRepository(service, dao)
    }
}