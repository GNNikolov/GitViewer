package com.gnnikolov.gitviewer.domain.model

data class Commit(
    val id: String,
    val gitRepositoryId: String,
    val sha: String,
    val message: String,
    val name: String,
    val date: String
)