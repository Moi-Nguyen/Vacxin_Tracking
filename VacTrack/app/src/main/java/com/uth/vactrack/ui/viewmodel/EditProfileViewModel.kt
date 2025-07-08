package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uth.vactrack.data.model.User
import com.uth.vactrack.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditProfileState(
    val isLoading: Boolean = false,
    val fullName: String = "",
    val nickName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val selectedGender: String = "",
    val expandedGender: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val success: Boolean = false
)

class EditProfileViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    
    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    init {
        loadCurrentUserProfile()
    }

    private fun loadCurrentUserProfile() {
        val currentUser = authRepository.getCurrentUser()
        currentUser?.let { user ->
            _state.value = _state.value.copy(
                fullName = user.name,
                email = user.email,
                phone = user.phone
            )
        }
    }

    fun updateFullName(name: String) {
        _state.value = _state.value.copy(fullName = name)
    }

    fun updateNickName(nickName: String) {
        _state.value = _state.value.copy(nickName = nickName)
    }

    fun updateEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun updatePhone(phone: String) {
        _state.value = _state.value.copy(phone = phone)
    }

    fun updateAddress(address: String) {
        _state.value = _state.value.copy(address = address)
    }

    fun updateGender(gender: String) {
        _state.value = _state.value.copy(
            selectedGender = gender,
            expandedGender = false
        )
    }

    fun toggleGenderDropdown() {
        _state.value = _state.value.copy(
            expandedGender = !_state.value.expandedGender
        )
    }

    fun isFormValid(): Boolean {
        val currentState = _state.value
        return currentState.fullName.isNotBlank() &&
                currentState.nickName.isNotBlank() &&
                currentState.email.isNotBlank() &&
                currentState.phone.isNotBlank() &&
                currentState.selectedGender.isNotBlank() &&
                currentState.address.isNotBlank()
    }

    fun updateProfile() {
        if (!isFormValid()) {
            _state.value = _state.value.copy(error = "Vui lòng điền đầy đủ thông tin")
            return
        }

        val currentState = _state.value
        
        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _state.value = _state.value.copy(error = "Email không hợp lệ")
            return
        }

        // Validate phone format
        if (currentState.phone.length < 10) {
            _state.value = _state.value.copy(error = "Số điện thoại không hợp lệ")
            return
        }

        _state.value = _state.value.copy(
            isLoading = true,
            error = null,
            message = null,
            success = false
        )

        viewModelScope.launch {
            try {
                // TODO: Implement API call to update user profile
                // For now, simulate success
                kotlinx.coroutines.delay(1000)
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    message = "Cập nhật thông tin thành công",
                    success = true
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Cập nhật thất bại",
                    success = false
                )
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