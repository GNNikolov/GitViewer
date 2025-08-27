package com.gnnikolov.gitviewer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommitDTO(
    @SerialName("node_id")
    val id: String,
    @SerialName("sha")
    val sha: String,
    @SerialName("commit")
    val details: CommitDetailsDTO
)
