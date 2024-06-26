package com.gnnikolov.gitviewer.di

import android.content.Context
import androidx.room.Room
import com.gnnikolov.gitviewer.data.database.AppDatabase
import com.gnnikolov.gitviewer.data.database.CommitDao
import com.gnnikolov.gitviewer.data.database.GitRepoModelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "git_viewer_db"
        ).build()
    }

    @Provides
    fun provideGitRepoDao(db: AppDatabase): GitRepoModelDao {
        return db.gitRepoDao()
    }

    @Provides
    fun provideCommitDao(db: AppDatabase): CommitDao {
        return db.commitDao()
    }
}