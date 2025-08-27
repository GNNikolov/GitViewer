package com.gnnikolov.gitviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gnnikolov.gitviewer.data.local.entity.CommitEntity

@Dao
abstract class CommitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg data: CommitEntity)

    @Query("SELECT * FROM CommitData WHERE gitRepositoryId = :id ORDER BY date(details_author_date) DESC LIMIT 1")
    abstract suspend fun getLastCommitForRepo(id: String): CommitEntity?
}