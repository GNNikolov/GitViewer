package com.gnnikolov.gitviewer.di

import com.gnnikolov.gitviewer.data.network.GitRepoApiService
import com.gnnikolov.gitviewer.data.network.UserApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        private const val URL = "https://api.github.com"
    }

    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    fun provideGitHubRetrofitService(): GitRepoApiService {
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(URL).build()
            .create(GitRepoApiService::class.java)
    }

    @Provides
    fun provideUserApiService(): UserApiService {
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(URL).build()
            .create(UserApiService::class.java)
    }
}