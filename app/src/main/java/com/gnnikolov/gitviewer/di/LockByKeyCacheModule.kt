package com.gnnikolov.gitviewer.di

import com.gnnikolov.gitviewer.concurrency.LockByKeyCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LockByKeyCacheModule {

    @Provides
    fun provideLockByCache(): LockByKeyCache<String> {
        return LockByKeyCache()
    }
}