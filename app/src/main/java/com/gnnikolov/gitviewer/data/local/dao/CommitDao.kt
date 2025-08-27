package com.gnnikolov.gitviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.gnnikolov.gitviewer.data.local.entity.CommitEntity

@Dao
abstract class CommitDao {

    @Upsert
    abstract fun insert(vararg data: CommitEntity)

    @Query("SELECT * FROM CommitData WHERE gitRepositoryId = :id ORDER BY date(details_author_date) DESC LIMIT 1")
    abstract suspend fun getLastCommitForRepo(id: String): CommitEntity?
}