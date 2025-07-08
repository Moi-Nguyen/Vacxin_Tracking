package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uth.vactrack.config.AppConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

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

    fun setOtp(otp: String) {
        _state.value = _state.value.copy(otp = otp)
    }

    fun verifyOtp(email: String) {
        val otp = _state.value.otp
        if (otp.length != 6) {
            _state.value = _state.value.copy(error = "OTP phải đủ 6 ký tự")
            return
        }
        _state.value = _state.value.copy(isLoading = true, error = null, success = false)
        viewModelScope.launch {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/auth/verify-otp")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                val body = JSONObject().apply {
                    put("email", email)
                    put("otp", otp)
                }.toString()
                conn.outputStream.use { it.write(body.toByteArray()) }
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                if (conn.responseCode == 200) {
                    _state.value = _state.value.copy(isLoading = false, resetToken = json.getString("resetToken"), success = true)
                } else {
                    _state.value = _state.value.copy(isLoading = false, error = json.optString("error", "OTP không hợp lệ"), success = false)
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.localizedMessage ?: "Lỗi xác thực OTP", success = false)
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