package com.gnnikolov.gitviewer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GitRepo")
data class GitRepoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val stars: Int,
    val watchers: Int,
    val name: String
)
