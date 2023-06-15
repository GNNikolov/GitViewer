package com.gnnikolov.gitviewer.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Author(
    @SerialName("name")
    val name: String,
    @SerialName("date")
    val date: String
)
