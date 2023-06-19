package com.gnnikolov.gitviewer.data.database

import androidx.room.*
import com.gnnikolov.gitviewer.data.model.GitRepoModel

@Dao
interface GitRepoModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg data: GitRepoModel)

    @Query("SELECT * FROM GitRepo")
    suspend fun getAll(): List<GitRepoModel>

}