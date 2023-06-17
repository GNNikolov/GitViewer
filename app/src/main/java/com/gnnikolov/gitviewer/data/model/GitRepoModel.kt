package com.gnnikolov.gitviewer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "GitRepo")
data class GitRepoModel(
    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @SerialName("stargazers_count")
    val stars: Int,
    @SerialName("watchers_count")
    val watchers: Int,
    @SerialName("name")
    val name: String
)
