package com.gnnikolov.gitviewer.domain

import com.gnnikolov.gitviewer.domain.model.User

interface IUserRepository {
    suspend fun getUsers(): List<User>

    suspend fun getUserForId(id: String): User
}