package com.gnnikolov.gitviewer.data.local.entity

import androidx.room.Embedded

data class CommitDetailsEntity(
    val message: String,
    @Embedded(prefix = "author_")
    val committer: AuthorEntity
)
