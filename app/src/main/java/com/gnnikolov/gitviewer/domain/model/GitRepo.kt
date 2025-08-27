package com.gnnikolov.gitviewer.domain.model

data class GitRepo(
    val id: String,
    val name: String,
    val stars: Int,
    val watchers: Int
)