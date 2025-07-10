package com.uth.vactrack.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.uth.vactrack.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import com.uth.vactrack.config.AppConfig

class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    suspend fun loginWithEmail(email: String, password: String): Result<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/auth/login")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                
                val body = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                }.toString()
                
                conn.outputStream.use { it.write(body.toByteArray()) }
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                
                if (conn.responseCode == 200) {
                    val userJson = json.getJSONObject("user")
                    val user = User(
                        id = userJson.optString("id", ""),
                        email = userJson.optString("email", ""),
                        name = userJson.optString("name", ""),
                        fullName = userJson.optString("fullName", null),
                        age = if (userJson.has("age")) userJson.optInt("age") else null,
                        dob = userJson.optString("dob", null),
                        address = userJson.optString("address", null),
                        phone = userJson.optString("phone", ""),
                        role = userJson.optString("role", null),
                        photoUrl = userJson.optString("photoUrl", "")
                    )
                    println("DEBUG EMAIL LOGIN USER PARSED: $user")
                    Result.success(AuthResponse(
                        token = json.getString("token"),
                        user = user,
                        message = json.getString("message")
                    ))
                } else {
                    Result.failure(Exception(json.optString("message", "Login failed")))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun register(email: String, password: String, name: String, phone: String): Result<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/auth/register")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                
                val body = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                    put("name", name)
                    put("phone", phone)
                }.toString()
                
                conn.outputStream.use { it.write(body.toByteArray()) }
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                
                if (conn.responseCode == 201) {
                    val user = User(
                        id = json.getJSONObject("user").optString("id", ""),
                        email = json.getJSONObject("user").optString("email", ""),
                        name = json.getJSONObject("user").optString("name", ""),
                        phone = json.getJSONObject("user").optString("phone", "")
                    )
                    Result.success(AuthResponse(
                        token = json.getString("token"),
                        user = user,
                        message = json.getString("message")
                    ))
                } else {
                    Result.failure(Exception(json.optString("message", "Registration failed")))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun loginWithGoogle(idToken: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = firebaseAuth.signInWithCredential(credential).await()
                val user = result.user
                println("DEBUG GOOGLE USER RAW: $user")
                if (user != null) {
                    val userModel = User(
                        id = user.uid,
                        email = user.email ?: "",
                        name = user.displayName ?: "",
                        phone = user.phoneNumber ?: "",
                        photoUrl = user.photoUrl?.toString() ?: ""
                    )
                    println("DEBUG GOOGLE USER PARSED: $userModel")
                    Result.success(userModel)
                } else {
                    Result.failure(Exception("Google login failed"))
                }
            } catch (e: Exception) {
                println("DEBUG GOOGLE LOGIN ERROR: ${e.message}")
                Result.failure(e)
            }
        }
    }

    suspend fun loginWithFacebook(accessToken: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val credential = FacebookAuthProvider.getCredential(accessToken)
                val result = firebaseAuth.signInWithCredential(credential).await()
                val user = result.user
                if (user != null) {
                    Result.success(User(
                        id = user.uid,
                        email = user.email ?: "",
                        name = user.displayName ?: "",
                        phone = user.phoneNumber ?: "",
                        photoUrl = user.photoUrl?.toString() ?: ""
                    ))
                } else {
                    Result.failure(Exception("Facebook login failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun forgotPassword(email: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/auth/request-reset")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                
                val body = JSONObject().apply {
                    put("email", email)
                }.toString()
                
                conn.outputStream.use { it.write(body.toByteArray()) }
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                
                if (conn.responseCode == 200) {
                    Result.success(json.getString("message"))
                } else {
                    Result.failure(Exception(json.optString("message", "Failed to send reset email")))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun resetPassword(token: String, newPassword: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/auth/set-new-password")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Authorization", "Bearer $token")
                conn.doOutput = true
                
                val body = JSONObject().apply {
                    put("newPassword", newPassword)
                }.toString()
                
                println("DEBUG RESET PASSWORD REQUEST: $body")
                println("DEBUG RESET PASSWORD TOKEN: $token")
                conn.outputStream.use { it.write(body.toByteArray()) }
                
                val responseCode = conn.responseCode
                println("DEBUG RESET PASSWORD RESPONSE CODE: $responseCode")
                
                val response = if (responseCode == 200) {
                    conn.inputStream.bufferedReader().readText()
                } else {
                    conn.errorStream.bufferedReader().readText()
                }
                println("DEBUG RESET PASSWORD RESPONSE: $response")
                
                val json = JSONObject(response)
                
                if (responseCode == 200) {
                    Result.success(json.getString("message"))
                } else {
                    val errorMessage = json.optString("message", "Failed to reset password")
                    println("DEBUG RESET PASSWORD ERROR: $errorMessage")
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                println("DEBUG RESET PASSWORD EXCEPTION: ${e.message}")
                Result.failure(e)
            }
        }
    }

    suspend fun verifyOtp(email: String, otp: String): Result<String> {
        return withContext(Dispatchers.IO) {
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

                println("DEBUG API REQUEST: $body")
                conn.outputStream.use { it.write(body.toByteArray()) }
                
                val responseCode = conn.responseCode
                println("DEBUG API RESPONSE CODE: $responseCode")
                
                val response = if (responseCode == 200) {
                    conn.inputStream.bufferedReader().readText()
                } else {
                    conn.errorStream.bufferedReader().readText()
                }
                println("DEBUG API RESPONSE: $response")
                
                val json = JSONObject(response)

                if (responseCode == 200) {
                    // Kiểm tra xem có resetToken không
                    val resetToken = json.optString("resetToken", "")
                    val message = json.optString("message", "OTP verified successfully")
                    println("DEBUG RESET TOKEN: $resetToken")
                    Result.success(resetToken.ifEmpty { message })
                } else {
                    val errorMessage = json.optString("message", "OTP verification failed")
                    println("DEBUG API ERROR: $errorMessage")
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                println("DEBUG API EXCEPTION: ${e.message}")
                Result.failure(e)
            }
        }
    }

    fun signOut(context: android.content.Context? = null) {
        firebaseAuth.signOut()
        // Google sign out
        try {
            if (context != null) {
                val googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(
                    context,
                    com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
                        com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
                    ).build()
                )
                googleSignInClient.signOut()
            }
        } catch (_: Exception) {}
    }

    fun getCurrentUser(): User? {
        val user = firebaseAuth.currentUser
        return user?.let {
            User(
                id = it.uid,
                email = it.email ?: "",
                name = it.displayName ?: "",
                phone = it.phoneNumber ?: ""
            )
        }
    }
} 