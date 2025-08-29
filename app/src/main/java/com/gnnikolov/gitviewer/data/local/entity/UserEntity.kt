package com.gnnikolov.gitviewer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val avatarUrl: String
)
