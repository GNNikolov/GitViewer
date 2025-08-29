package com.gnnikolov.gitviewer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "GitRepo",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId"),
            onDelete = CASCADE
        )
    ]
)
data class GitRepoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val stars: Int,
    val watchers: Int,
    val name: String,
    val userId: String
)
