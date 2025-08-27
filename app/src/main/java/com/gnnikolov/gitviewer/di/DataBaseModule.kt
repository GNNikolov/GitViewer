package com.gnnikolov.gitviewer.di

import android.content.Context
import androidx.room.Room
import com.gnnikolov.gitviewer.data.local.database.AppDatabase
import com.gnnikolov.gitviewer.data.local.dao.CommitDao
import com.gnnikolov.gitviewer.data.local.dao.GitRepoDao
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
        ).addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3).build()
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