package com.gnnikolov.gitviewer.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

//FIXME: Provide this with DI!!!
object GitHubApi {
    private const val URL = "https://api.github.com"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json {
            ignoreUnknownKeys = true
        }.asConverterFactory("application/json".toMediaType()))
        .baseUrl(URL).build()

    val gitRepoService by lazy { retrofit.create(GitRepoService::class.java) }
}