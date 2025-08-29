package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.data.local.dao.UserDao
import com.gnnikolov.gitviewer.data.network.datasource.UserNetworkDataSource
import com.gnnikolov.gitviewer.domain.IUserRepository
import com.gnnikolov.gitviewer.domain.model.User
import com.gnnikolov.gitviewer.mappers.toDomain
import com.gnnikolov.gitviewer.mappers.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserNetworkDataSource,
    private val dao: UserDao,
) : IUserRepository {

    override suspend fun getUsers(): List<User> {
        val remoteData = dataSource.getUsers()
        if (remoteData.isSuccess) {
            remoteData.getOrNull()?.let {
                dao.insert(*it.map { item -> item.toEntity() }.toTypedArray())
            }
        }
        return dao.getAll().map { it.toDomain() }
    }

}