package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uth.vactrack.data.model.User
import com.uth.vactrack.data.repository.AuthRepository
import com.uth.vactrack.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SharedState(
    val currentUser: User? = null,
    val isDarkTheme: Boolean = false,
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val userId: String? = null,
    val selectedServiceId: String? = null,
    val selectedFacilityId: String? = null
)

class SharedViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    // Thêm HomeViewModel để đồng bộ bookings
    val homeViewModel: HomeViewModel = HomeViewModel()
    
    private val _sharedState = MutableStateFlow(SharedState())
    val sharedState: StateFlow<SharedState> = _sharedState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            _sharedState.value = _sharedState.value.copy(
                currentUser = currentUser,
                isLoggedIn = true,
                userId = currentUser.id
            )
        }
    }

    fun setCurrentUser(user: User) {
        _sharedState.value = _sharedState.value.copy(
            currentUser = user,
            isLoggedIn = true,
            userId = user.id
        )
    }

    fun clearCurrentUser() {
        _sharedState.value = _sharedState.value.copy(
            currentUser = null,
            isLoggedIn = false,
            userId = null,
            token = null
        )
    }

    fun toggleTheme() {
        _sharedState.value = _sharedState.value.copy(
            isDarkTheme = !_sharedState.value.isDarkTheme
        )
    }

    fun setTheme(isDark: Boolean) {
        _sharedState.value = _sharedState.value.copy(
            isDarkTheme = isDark
        )
    }

    fun logout(context: android.content.Context? = null) {
        authRepository.signOut(context)
        clearCurrentUser()
    }

    fun setToken(token: String) {
        _sharedState.value = _sharedState.value.copy(token = token)
    }
    
    fun setUserId(userId: String) {
        _sharedState.value = _sharedState.value.copy(userId = userId)
    }
    
    fun setSelectedServiceId(serviceId: String) {
        _sharedState.value = _sharedState.value.copy(selectedServiceId = serviceId)
    }
    
    fun setSelectedFacilityId(facilityId: String) {
        _sharedState.value = _sharedState.value.copy(selectedFacilityId = facilityId)
    }
} 