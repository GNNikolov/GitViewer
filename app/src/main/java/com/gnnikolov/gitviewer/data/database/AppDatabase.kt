package com.gnnikolov.gitviewer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gnnikolov.gitviewer.data.model.GitRepoModel

@Database(entities = [GitRepoModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gitRepoDao() : GitRepoModelDao
}