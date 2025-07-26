package com.gnnikolov.gitviewer.di

import com.gnnikolov.gitviewer.FakeCommitsRepository
import com.gnnikolov.gitviewer.FakeGitRepoModelsRepository
import com.gnnikolov.gitviewer.data.repository.ICommitsRepository
import com.gnnikolov.gitviewer.data.repository.IGitRepoModelsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RepositoryModule::class])
abstract class FakeRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGitRepoModelRepository(impl: FakeGitRepoModelsRepository): IGitRepoModelsRepository

    @Binds
    @Singleton
    abstract fun bindCommitsRepository(impl: FakeCommitsRepository): ICommitsRepository
}