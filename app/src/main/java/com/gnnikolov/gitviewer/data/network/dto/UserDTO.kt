package com.gnnikolov.gitviewer.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    @SerialName("node_id")
    val id: String,
    @SerialName("login")
    val name: String,
    @SerialName("avatar_url")
    val avatarUrl: String
)
