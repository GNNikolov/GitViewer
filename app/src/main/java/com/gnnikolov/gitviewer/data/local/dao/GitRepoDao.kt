package com.gnnikolov.gitviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.gnnikolov.gitviewer.data.local.entity.GitRepoEntity

@Dao
interface GitRepoDao {

    @Upsert
    suspend fun insertAll(vararg data: GitRepoEntity)

    @Query("SELECT * FROM GitRepo WHERE userId = :id")
    suspend fun getAllForUser(id: String): List<GitRepoEntity>

}