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
                if (user != null) {
                    Result.success(User(
                        id = user.uid,
                        email = user.email ?: "",
                        name = user.displayName ?: "",
                        phone = user.phoneNumber ?: "",
                        photoUrl = user.photoUrl?.toString() ?: ""
                    ))
                } else {
                    Result.failure(Exception("Google login failed"))
                }
            } catch (e: Exception) {
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
                val url = URL("${AppConfig.BASE_URL}/api/auth/forgot-password")
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
                val url = URL("${AppConfig.BASE_URL}/api/auth/reset-password")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                
                val body = JSONObject().apply {
                    put("token", token)
                    put("newPassword", newPassword)
                }.toString()
                
                conn.outputStream.use { it.write(body.toByteArray()) }
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                
                if (conn.responseCode == 200) {
                    Result.success(json.getString("message"))
                } else {
                    Result.failure(Exception(json.optString("message", "Failed to reset password")))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun signOut(context: android.content.Context? = null) {
        firebaseAuth.signOut()
        // Google sign out nếu cần
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