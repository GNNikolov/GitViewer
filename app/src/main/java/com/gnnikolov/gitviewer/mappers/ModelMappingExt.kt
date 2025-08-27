package com.gnnikolov.gitviewer.mappers

import com.gnnikolov.gitviewer.data.local.entity.AuthorEntity
import com.gnnikolov.gitviewer.data.local.entity.CommitDetailsEntity
import com.gnnikolov.gitviewer.data.local.entity.CommitEntity
import com.gnnikolov.gitviewer.data.local.entity.GitRepoEntity
import com.gnnikolov.gitviewer.data.remote.dto.AuthorDTO
import com.gnnikolov.gitviewer.data.remote.dto.CommitDTO
import com.gnnikolov.gitviewer.data.remote.dto.CommitDetailsDTO
import com.gnnikolov.gitviewer.data.remote.dto.GitRepoDTO
import com.gnnikolov.gitviewer.domain.model.Commit
import com.gnnikolov.gitviewer.domain.model.GitRepo

fun GitRepoDTO.toEntity() = GitRepoEntity(id = id, name = name, stars = stars, watchers = watchers)

fun GitRepoEntity.toDomain() = GitRepo(id = id, name = name, stars = stars, watchers = watchers)

fun AuthorDTO.toEntity() = AuthorEntity(name = name, date = date)

fun CommitDetailsDTO.toEntity() =
    CommitDetailsEntity(message = message, committer = committer.toEntity())

fun CommitDTO.toEntity(foreignKey: String) = CommitEntity(
    id = id,
    gitRepositoryId = foreignKey,
    sha = sha,
    details = details.toEntity()
)

fun CommitEntity.toDomain() = Commit(
    id = this.id,
    gitRepositoryId = this.gitRepositoryId,
    sha = this.sha,
    message = this.details.message,
    name = this.details.committer.name,
    date = this.details.committer.date
)
