package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uth.vactrack.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ForgotPasswordState(
    val isLoading: Boolean = false,
    val email: String = "",
    val error: String? = null,
    val message: String? = null,
    val success: Boolean = false
)

class ForgotPasswordViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val _state = MutableStateFlow(ForgotPasswordState())
    val state: StateFlow<ForgotPasswordState> = _state.asStateFlow()

    fun setEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun requestReset() {
        val email = _state.value.email
        if (email.isBlank()) {
            _state.value = _state.value.copy(error = "Email không được để trống")
            return
        }
        _state.value = _state.value.copy(isLoading = true, error = null, message = null, success = false)
        viewModelScope.launch {
            authRepository.forgotPassword(email)
                .onSuccess { message ->
                    _state.value = _state.value.copy(isLoading = false, message = message, success = true)
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(isLoading = false, error = exception.message ?: "Request failed", success = false)
                }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    fun clearMessage() {
        _state.value = _state.value.copy(message = null)
    }
    fun resetSuccess() {
        _state.value = _state.value.copy(success = false)
    }
} 