package com.gnnikolov.gitviewer.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Commit(
    @SerialName("sha")
    val sha: String,
    @SerialName("commit")
    val details: CommitDetails
)