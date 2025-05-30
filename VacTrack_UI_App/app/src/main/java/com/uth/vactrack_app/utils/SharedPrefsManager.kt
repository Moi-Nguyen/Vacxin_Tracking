package com.uth.vactrack_app.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.uth.vactrack_app.data.models.UserData

class SharedPrefsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "VacTrack_Prefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER = "user_data"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // Lưu thông tin đăng nhập
    fun saveLoginData(token: String, user: UserData) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USER, gson.toJson(user))
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    // Lấy token
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    // Lấy thông tin user
    fun getUser(): UserData? {
        val userJson = prefs.getString(KEY_USER, null)
        return if (userJson != null) {
            gson.fromJson(userJson, UserData::class.java)
        } else null
    }

    // Kiểm tra đã đăng nhập chưa
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    // Đăng xuất
    fun logout() {
        prefs.edit()
            .remove(KEY_TOKEN)
            .remove(KEY_USER)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }

    // Clear all data
    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
