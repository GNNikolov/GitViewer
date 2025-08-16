package com.gnnikolov.gitviewer.ui.state

import androidx.compose.runtime.Immutable

@Immutable
sealed interface Async<out T> {

    @Immutable
    object Loading : Async<Nothing>

    @Immutable
    data class Error(val errorMessage: Int) : Async<Nothing>

    @Immutable
    data class Success<out T>(val data: T) : Async<T>

}