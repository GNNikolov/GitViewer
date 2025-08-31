package com.gnnikolov.gitviewer.data.repository

import com.gnnikolov.gitviewer.data.local.dao.UserDao
import com.gnnikolov.gitviewer.data.network.datasource.UserNetworkDataSource
import com.gnnikolov.gitviewer.di.IoDispatcher
import com.gnnikolov.gitviewer.domain.IUserRepository
import com.gnnikolov.gitviewer.domain.model.User
import com.gnnikolov.gitviewer.toDomain
import com.gnnikolov.gitviewer.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserNetworkDataSource,
    private val dao: UserDao,
    @IoDispatcher private val ioDispatchers: CoroutineDispatcher
) : IUserRepository {

    override suspend fun getUsers(): List<User> = withContext(ioDispatchers) {
        val remoteData = dataSource.getUsers()
        if (remoteData.isSuccess) {
            remoteData.getOrNull()?.let {
                dao.insert(*it.map { item -> item.toEntity() }.toTypedArray())
            }
        }
        return@withContext dao.getAll().map { it.toDomain() }
    }

    override suspend fun getUserForId(id: String): User = withContext(ioDispatchers) {
        dao.getUserById(id).toDomain()
    }
}