package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uth.vactrack.data.model.User
import com.uth.vactrack.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SharedState(
    val currentUser: User? = null,
    val isDarkTheme: Boolean = false,
    val isLoggedIn: Boolean = false
)

class SharedViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    
    private val _sharedState = MutableStateFlow(SharedState())
    val sharedState: StateFlow<SharedState> = _sharedState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            _sharedState.value = _sharedState.value.copy(
                currentUser = currentUser,
                isLoggedIn = true
            )
        }
    }

    fun setCurrentUser(user: User) {
        _sharedState.value = _sharedState.value.copy(
            currentUser = user,
            isLoggedIn = true
        )
    }

    fun clearCurrentUser() {
        _sharedState.value = _sharedState.value.copy(
            currentUser = null,
            isLoggedIn = false
        )
    }

    fun toggleTheme() {
        _sharedState.value = _sharedState.value.copy(
            isDarkTheme = !_sharedState.value.isDarkTheme
        )
    }

    fun setTheme(isDark: Boolean) {
        _sharedState.value = _sharedState.value.copy(
            isDarkTheme = isDark
        )
    }

    fun logout(context: android.content.Context? = null) {
        authRepository.signOut(context)
        clearCurrentUser()
    }
} 