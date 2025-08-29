package com.gnnikolov.gitviewer.data.network

import com.gnnikolov.gitviewer.data.network.dto.UserDTO
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    @GET("/users")
    suspend fun getUsers(): Response<List<UserDTO>>
}