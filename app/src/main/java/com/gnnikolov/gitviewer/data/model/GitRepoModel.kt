package com.gnnikolov.gitviewer.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitRepoModel(
    @SerialName("id")
    val id: Long,
    @SerialName("stargazers_count")
    val stars: Int,
    @SerialName("watchers_count")
    val watchers: Int,
    @SerialName("name")
    val name: String
)
