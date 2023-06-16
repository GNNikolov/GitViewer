package com.gnnikolov.gitviewer.di

import com.gnnikolov.gitviewer.data.CommitsRepository
import com.gnnikolov.gitviewer.data.GitRepoModelsRepository
import com.gnnikolov.gitviewer.data.remote.GitRepoService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideGitRepoModelRepository(service: GitRepoService): GitRepoModelsRepository {
        return GitRepoModelsRepository(service)
    }

    @Provides
    @Singleton
    fun provideCommitsRepository(service: GitRepoService): CommitsRepository {
        return CommitsRepository(service)
    }
}