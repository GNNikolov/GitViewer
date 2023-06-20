package com.gnnikolov.gitviewer.data.database

import androidx.room.*
import com.gnnikolov.gitviewer.data.model.Commit
import com.gnnikolov.gitviewer.data.model.GitRepoModel

@Dao
abstract class CommitDao {

    @Transaction
    open fun insert(gitRepoModel: GitRepoModel, vararg data: Commit) {
        data.forEach {
            it.gitRepositoryId = gitRepoModel.id
            insert(it)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data: Commit)

    @Query("SELECT * FROM CommitData WHERE gitRepositoryId = :id ORDER BY date(details_author_date) DESC LIMIT 1")
    abstract suspend fun getLastCommitForRepo(id: Long): Commit?
}