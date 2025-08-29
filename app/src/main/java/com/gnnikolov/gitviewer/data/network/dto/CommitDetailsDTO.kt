package com.gnnikolov.gitviewer.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommitDetailsDTO(
    @SerialName("message")
    val message: String,
    @SerialName("committer")
    val committer: AuthorDTO
)
