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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class Users(val value: List<User>)

@Immutable
data class UsersUiState(
    val usersState: Async<Users> = Async.Loading,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val resultUsers: Users = Users(emptyList())
)

@HiltViewModel
class UsersViewModel @Inject constructor(private val repository: IUserRepository) : ViewModel() {

    private val _allUsersState = MutableStateFlow<Async<Users>>(Async.Loading)
    private val _searchQuery = MutableStateFlow("")
    private val _isSearchActive = MutableStateFlow(false)

    val uiState: StateFlow<UsersUiState> =
        combine(
            flow = _allUsersState,
            flow2 = _searchQuery,
            flow3 = _isSearchActive
        ) { allState, query, isActive ->
            val resultUsers = when (allState) {
                is Async.Success -> Users(allState.data.value.filter {
                    it.name.contains(query, ignoreCase = true)
                })
                else -> Users(emptyList())
            }
            UsersUiState(allState, query, isActive, resultUsers)
        }.onStart {
            fetchUsers()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UsersUiState()
        )

    fun fetchUsers() {
        viewModelScope.launch {
            val result = repository.getUsers()
            _allUsersState.update {
                if (result.isEmpty())
                    Async.Error
                else
                    Async.Success(Users(result))
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
        if (!active)
            _searchQuery.value = ""
    }
}