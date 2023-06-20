package com.gnnikolov.gitviewer.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "CommitData",
    foreignKeys = [
        ForeignKey(
            entity = GitRepoModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("gitRepositoryId"),
            onDelete = CASCADE
        )]
)
data class Commit(
    @PrimaryKey(autoGenerate = false)
    @SerialName("node_id")
    val id: String,
    @kotlinx.serialization.Transient
    var gitRepositoryId: Long = -1,
    @SerialName("sha")
    val sha: String,
    @SerialName("commit")
    @Embedded(prefix = "details_")
    val details: CommitDetails
)