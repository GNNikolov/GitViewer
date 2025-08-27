package com.gnnikolov.gitviewer.di

import com.gnnikolov.gitviewer.data.repository.CommitsRepositoryImpl
import com.gnnikolov.gitviewer.data.repository.GitRepoModelsRepositoryImpl
import com.gnnikolov.gitviewer.domain.ICommitsRepository
import com.gnnikolov.gitviewer.domain.IGitRepoModelsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGitRepoModelRepository(impl: GitRepoModelsRepositoryImpl): IGitRepoModelsRepository

    @Binds
    @Singleton
    abstract fun bindCommitsRepository(impl: CommitsRepositoryImpl): ICommitsRepository
}