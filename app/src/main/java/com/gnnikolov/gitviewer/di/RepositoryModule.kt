package com.gnnikolov.gitviewer.di

import com.gnnikolov.gitviewer.data.repository.CommitsRepositoryImpl
import com.gnnikolov.gitviewer.data.repository.GitRepoRepositoryImpl
import com.gnnikolov.gitviewer.data.repository.UserRepositoryImpl
import com.gnnikolov.gitviewer.domain.ICommitsRepository
import com.gnnikolov.gitviewer.domain.IGitRepoRepository
import com.gnnikolov.gitviewer.domain.IUserRepository
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
    abstract fun bindGitRepoModelRepository(impl: GitRepoRepositoryImpl): IGitRepoRepository

    @Binds
    @Singleton
    abstract fun bindCommitsRepository(impl: CommitsRepositoryImpl): ICommitsRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): IUserRepository
}