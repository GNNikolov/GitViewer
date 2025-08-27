package com.gnnikolov.gitviewer.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "CommitData",
    foreignKeys = [
        ForeignKey(
            entity = GitRepoEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("gitRepositoryId"),
            onDelete = CASCADE
        )]
)
data class CommitEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val gitRepositoryId: String,
    val sha: String,
    @Embedded(prefix = "details_")
    val details: CommitDetailsEntity
)
