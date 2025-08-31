package com.gnnikolov.gitviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.gnnikolov.gitviewer.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Upsert
    suspend fun insert(vararg data: UserEntity)

    @Query("SELECT * FROM User")
    suspend fun getAll(): List<UserEntity>

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity
}