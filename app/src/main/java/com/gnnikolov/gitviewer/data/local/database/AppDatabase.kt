package com.gnnikolov.gitviewer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gnnikolov.gitviewer.data.local.dao.CommitDao
import com.gnnikolov.gitviewer.data.local.dao.GitRepoDao
import com.gnnikolov.gitviewer.data.local.entity.CommitEntity
import com.gnnikolov.gitviewer.data.local.entity.GitRepoEntity

@Database(entities = [GitRepoEntity::class, CommitEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gitRepoDao(): GitRepoDao
    abstract fun commitDao(): CommitDao
}