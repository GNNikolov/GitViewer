package com.gnnikolov.gitviewer.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gnnikolov.gitviewer.data.model.GitRepoModel

@Dao
interface GitRepoModelDao {

    @Insert
    suspend fun insertAll(vararg data: GitRepoModel)

    @Query("SELECT * FROM GitRepo")
    suspend fun getAll(): List<GitRepoModel>

    @Query("DELETE FROM GitRepo")
    suspend fun deleteAll()
}