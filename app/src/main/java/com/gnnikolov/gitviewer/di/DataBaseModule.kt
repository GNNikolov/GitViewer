package com.gnnikolov.gitviewer.di

import android.content.Context
import androidx.room.Room
import com.gnnikolov.gitviewer.data.database.AppDatabase
import com.gnnikolov.gitviewer.data.database.GitRepoModelDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun provideDb(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "git_viewer_db"
        ).build()
    }

    @Provides
    fun provideGitRepoDao(db: AppDatabase): GitRepoModelDao {
        return db.gitRepoDao()
    }
}