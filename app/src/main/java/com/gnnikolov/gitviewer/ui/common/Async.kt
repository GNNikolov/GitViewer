package com.gnnikolov.gitviewer.ui.common

import androidx.compose.runtime.Immutable

@Immutable
sealed interface Async<out T> {

    @Immutable
    data object Loading : Async<Nothing>

    @Immutable
    data object Error : Async<Nothing>

    @Immutable
    data class Success<out T>(val data: T) : Async<T>

}