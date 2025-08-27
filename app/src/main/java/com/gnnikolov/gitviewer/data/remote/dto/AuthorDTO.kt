package com.gnnikolov.gitviewer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDTO(
    @SerialName("name")
    val name: String,
    @SerialName("date")
    val date: String
)
