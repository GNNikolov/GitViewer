package com.gnnikolov.gitviewer.ui.users

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnnikolov.gitviewer.domain.IUserRepository
import com.gnnikolov.gitviewer.domain.model.User
import com.gnnikolov.gitviewer.ui.common.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class Users(val value: List<User>)

@HiltViewModel
class UsersViewModel @Inject constructor(private val repository: IUserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<Async<Users>>(Async.Loading)
    val uiState = _uiState.onStart { fetchUsers() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = Async.Loading
    )

    fun fetchUsers() {
        viewModelScope.launch {
            val result = repository.getUsers()
            _uiState.update { if (result.isEmpty()) Async.Error else Async.Success(Users(result)) }
        }
    }
}