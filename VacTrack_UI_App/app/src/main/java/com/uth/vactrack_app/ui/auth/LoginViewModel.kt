package com.uth.vactrack_app.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uth.vactrack_app.data.repository.AuthRepository
import com.uth.vactrack_app.data.models.LoginResponse
import com.uth.vactrack_app.utils.SharedPrefsManager
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository()
    private val sharedPrefsManager = SharedPrefsManager(application)

    // LiveData cho UI state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoginSuccessful = MutableLiveData<Boolean>()
    val isLoginSuccessful: LiveData<Boolean> = _isLoginSuccessful

    init {
        _isLoading.value = false
        _isLoginSuccessful.value = false
        _errorMessage.value = ""
    }

    // Validation functions
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    // Login function
    fun login(email: String, password: String) {
        // Validate input
        if (!isValidEmail(email)) {
            _errorMessage.value = "Please enter a valid email address"
            return
        }

        if (!isValidPassword(password)) {
            _errorMessage.value = "Password must be at least 6 characters"
            return
        }

        _isLoading.value = true
        _errorMessage.value = ""

        viewModelScope.launch {
            try {
                val result = authRepository.login(email, password)
                _loginResult.value = result

                result.onSuccess { loginResponse ->
                    // Save login data
                    sharedPrefsManager.saveLoginData(loginResponse.token, loginResponse.user)
                    _isLoginSuccessful.value = true
                    _errorMessage.value = ""
                }.onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Login failed"
                    _isLoginSuccessful.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
                _isLoginSuccessful.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Google login
    fun googleLogin(token: String) {
        _isLoading.value = true
        _errorMessage.value = ""

        viewModelScope.launch {
            try {
                val result = authRepository.googleLogin(token)
                result.onSuccess { loginResponse ->
                    sharedPrefsManager.saveLoginData(loginResponse.token, loginResponse.user)
                    _isLoginSuccessful.value = true
                    _errorMessage.value = ""
                }.onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Google login failed"
                    _isLoginSuccessful.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
                _isLoginSuccessful.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Forgot password
    fun forgotPassword(email: String) {
        if (!isValidEmail(email)) {
            _errorMessage.value = "Please enter a valid email address"
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val result = authRepository.forgotPassword(email)
                result.onSuccess { message ->
                    _errorMessage.value = "Reset email sent successfully!"
                }.onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Failed to send reset email"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Clear error message
    fun clearError() {
        _errorMessage.value = ""
    }

    // Check if user is already logged in
    fun checkLoginStatus(): Boolean {
        return sharedPrefsManager.isLoggedIn()
    }

    // Logout function
    fun logout() {
        sharedPrefsManager.logout()
        _isLoginSuccessful.value = false
    }
}
