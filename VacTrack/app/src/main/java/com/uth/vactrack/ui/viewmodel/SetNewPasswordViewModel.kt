package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uth.vactrack.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SetNewPasswordState(
    val isLoading: Boolean = false,
    val newPassword: String = "",
    val error: String? = null,
    val message: String? = null,
    val success: Boolean = false
)

class SetNewPasswordViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val _state = MutableStateFlow(SetNewPasswordState())
    val state: StateFlow<SetNewPasswordState> = _state.asStateFlow()

    fun setNewPassword(password: String) {
        _state.value = _state.value.copy(newPassword = password)
    }

    fun resetPassword(token: String) {
        val password = _state.value.newPassword
        if (password.length < 6) {
            _state.value = _state.value.copy(error = "Mật khẩu phải từ 6 ký tự")
            return
        }
        _state.value = _state.value.copy(isLoading = true, error = null, message = null, success = false)
        viewModelScope.launch {
            authRepository.resetPassword(token, password)
                .onSuccess { message ->
                    _state.value = _state.value.copy(isLoading = false, message = message, success = true)
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(isLoading = false, error = exception.message ?: "Reset failed", success = false)
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