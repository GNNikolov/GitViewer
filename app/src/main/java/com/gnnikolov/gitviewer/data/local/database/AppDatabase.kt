package com.gnnikolov.gitviewer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gnnikolov.gitviewer.data.local.dao.CommitDao
import com.gnnikolov.gitviewer.data.local.dao.GitRepoDao
import com.gnnikolov.gitviewer.data.local.entity.CommitEntity
import com.gnnikolov.gitviewer.data.local.entity.GitRepoEntity

@Database(entities = [GitRepoEntity::class, CommitEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gitRepoDao(): GitRepoDao
    abstract fun commitDao(): CommitDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                CREATE TABLE CommitData_tmp(
                    id TEXT PRIMARY KEY NOT NULL,
                    gitRepositoryId TEXT NOT NULL,
                    sha TEXT NOT NULL,
                    details_message TEXT NOT NULL,
                    details_author_name TEXT NOT NULL, 
                    details_author_date TEXT NOT NULL,
                    FOREIGN KEY (gitRepositoryId) REFERENCES GitRepo(id) ON DELETE CASCADE
                )
            """.trimIndent()
                )
                database.execSQL(
                    """
                INSERT INTO CommitData_tmp (id, gitRepositoryId, sha, details_message, details_author_name, details_author_date)
                SELECT id, gitRepositoryId, sha, details_message, details_author_name, details_author_date FROM CommitData
            """.trimIndent()
                )
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    DROP TABLE CommitData
                """.trimIndent())
                database.execSQL("""
                    ALTER TABLE CommitData_tmp RENAME TO CommitData
                """.trimIndent())
            }
        }
    }

}