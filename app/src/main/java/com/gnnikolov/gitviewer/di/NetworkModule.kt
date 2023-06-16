package com.gnnikolov.gitviewer.di

import com.gnnikolov.gitviewer.data.remote.GitRepoService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Module
class NetworkModule {

    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    fun provideGitHubRetrofitService(): GitRepoService {
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://api.github.com").build()
            .create(GitRepoService::class.java)
    }

}