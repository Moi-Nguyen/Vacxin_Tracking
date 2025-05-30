package com.uth.vactrack_app.data.repository

import com.uth.vactrack_app.data.models.LoginRequest
import com.uth.vactrack_app.data.models.LoginResponse
import com.uth.vactrack_app.data.network.NetworkConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    private val apiService = NetworkConfig.apiService

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(email, password)
                val response = apiService.login(request)

                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        // API trả về thành công khi có response.body()
                        Result.success(loginResponse)
                    } ?: Result.failure(Exception("Empty response body"))
                } else {
                    // Xử lý error response
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Login failed: $errorBody"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Network error: ${e.message}"))
            }
        }
    }

    suspend fun googleLogin(token: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.googleLogin(token)
                if (response.isSuccessful) {
                    response.body()?.let { Result.success(it) }
                        ?: Result.failure(Exception("Empty response"))
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Google login failed: $errorBody"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Network error: ${e.message}"))
            }
        }
    }

    suspend fun forgotPassword(email: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.forgotPassword(mapOf("email" to email))
                if (response.isSuccessful) {
                    // Giả sử forgot password cũng có format tương tự
                    Result.success("Password reset email sent successfully")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Failed to send reset email: $errorBody"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Network error: ${e.message}"))
            }
        }
    }
}
