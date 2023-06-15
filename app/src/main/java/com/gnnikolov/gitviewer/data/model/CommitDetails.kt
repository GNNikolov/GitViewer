package com.gnnikolov.gitviewer.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommitDetails(
    @SerialName("message")
    val message: String,
    @SerialName("committer")
    val committer: Author
)
