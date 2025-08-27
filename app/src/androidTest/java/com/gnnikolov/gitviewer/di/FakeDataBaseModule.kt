package com.gnnikolov.gitviewer.di

import android.content.Context
import androidx.room.Room
import com.gnnikolov.gitviewer.data.local.database.AppDatabase
import com.gnnikolov.gitviewer.data.local.dao.CommitDao
import com.gnnikolov.gitviewer.data.local.dao.GitRepoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DataBaseModule::class])
class FakeDataBaseModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
    }

    @Provides
    fun provideGitRepoDao(db: AppDatabase): GitRepoDao {
        return db.gitRepoDao()
    }

    @Provides
    fun provideCommitDao(db: AppDatabase): CommitDao {
        return db.commitDao()
    }
}