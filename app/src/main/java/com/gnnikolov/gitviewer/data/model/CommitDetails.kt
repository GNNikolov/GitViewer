package com.gnnikolov.gitviewer.data.model

import androidx.room.Embedded
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommitDetails(
    @SerialName("message")
    val message: String,
    @SerialName("committer")
    @Embedded(prefix = "author_")
    val committer: Author
)
