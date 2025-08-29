package com.gnnikolov.gitviewer.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitRepoDTO(
    @SerialName("node_id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("stargazers_count")
    val stars: Int,
    @SerialName("watchers_count")
    val watchers: Int
)
