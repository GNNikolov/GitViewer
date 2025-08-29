package com.gnnikolov.gitviewer.mappers

import com.gnnikolov.gitviewer.data.local.entity.AuthorEntity
import com.gnnikolov.gitviewer.data.local.entity.CommitDetailsEntity
import com.gnnikolov.gitviewer.data.local.entity.CommitEntity
import com.gnnikolov.gitviewer.data.local.entity.GitRepoEntity
import com.gnnikolov.gitviewer.data.local.entity.UserEntity
import com.gnnikolov.gitviewer.data.network.dto.AuthorDTO
import com.gnnikolov.gitviewer.data.network.dto.CommitDTO
import com.gnnikolov.gitviewer.data.network.dto.CommitDetailsDTO
import com.gnnikolov.gitviewer.data.network.dto.GitRepoDTO
import com.gnnikolov.gitviewer.data.network.dto.UserDTO
import com.gnnikolov.gitviewer.domain.model.Commit
import com.gnnikolov.gitviewer.domain.model.GitRepo
import com.gnnikolov.gitviewer.domain.model.User

fun GitRepoDTO.toEntity(foreignKey: String) =
    GitRepoEntity(id = id, name = name, stars = stars, watchers = watchers, userId = foreignKey)

fun GitRepoEntity.toDomain() =
    GitRepo(id = id, name = name, stars = stars, watchers = watchers, userId = userId)

fun GitRepo.toEntity() =
    GitRepoEntity(id = id, name = name, stars = stars, watchers = watchers, userId = userId)

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

fun UserDTO.toEntity() = UserEntity(id = id, name = name, avatarUrl = avatarUrl)

fun UserEntity.toDomain() = User(id = id, name = name, avatarUrl = avatarUrl)

fun User.toEntity() = UserEntity(id = id, name = name, avatarUrl = avatarUrl)
