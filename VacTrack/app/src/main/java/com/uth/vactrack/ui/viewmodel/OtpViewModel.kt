package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uth.vactrack.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State cho OTP
data class OtpState(
    val isLoading: Boolean = false,
    val otp: String = "",
    val error: String? = null,
    val resetToken: String? = null,
    val success: Boolean = false
)

class OtpViewModel : ViewModel() {
    private val _state = MutableStateFlow(OtpState())
    val state: StateFlow<OtpState> = _state.asStateFlow()
    
    private val authRepository = AuthRepository()

    fun setOtp(otp: String) {
        _state.value = _state.value.copy(otp = otp)
    }

    fun verifyOtp(email: String) {
        val otp = _state.value.otp
        println("DEBUG OTP: '$otp', EMAIL: '$email'")
        
        if (otp.length != 6) {
            _state.value = _state.value.copy(error = "OTP phải đủ 6 ký tự")
            return
        }
        
        _state.value = _state.value.copy(isLoading = true, error = null, success = false)
        
        viewModelScope.launch {
            try {
                val result = authRepository.verifyOtp(email, otp)
                result.fold(
                    onSuccess = { resetTokenOrMessage ->
                        println("DEBUG OTP SUCCESS: $resetTokenOrMessage")
                        // Nếu API trả về resetToken thì dùng, không thì tạo tạm thời
                        val token = if (resetTokenOrMessage.startsWith("temp_token_") || resetTokenOrMessage.length > 20) {
                            resetTokenOrMessage
                        } else {
                            "temp_token_$email"
                        }
                        _state.value = _state.value.copy(
                            isLoading = false,
                            resetToken = token,
                            success = true
                        )
                    },
                    onFailure = { exception ->
                        println("DEBUG OTP ERROR: ${exception.message}")
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = exception.message ?: "OTP không hợp lệ",
                            success = false
                        )
                    }
                )
            } catch (e: Exception) {
                println("DEBUG OTP EXCEPTION: ${e.message}")
                _state.value = _state.value.copy(
                    isLoading = false, 
                    error = e.localizedMessage ?: "Lỗi xác thực OTP", 
                    success = false
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    fun resetSuccess() {
        _state.value = _state.value.copy(success = false, resetToken = null)
    }
} 