package com.uth.vactrack_app.data.network

import com.uth.vactrack_app.data.models.ApiResponse
import com.uth.vactrack_app.data.models.LoginRequest
import com.uth.vactrack_app.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/auth/google")
    suspend fun googleLogin(@Query("token") token: String): Response<LoginResponse>

    @GET("api/auth/facebook")
    suspend fun facebookLogin(@Query("token") token: String): Response<LoginResponse>

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body email: Map<String, String>): Response<ApiResponse<Any>>
}