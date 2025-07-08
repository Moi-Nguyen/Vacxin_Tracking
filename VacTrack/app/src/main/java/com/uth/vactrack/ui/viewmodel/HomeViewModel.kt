package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeState(
    val menuExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun toggleMenu() {
        _state.value = _state.value.copy(
            menuExpanded = !_state.value.menuExpanded
        )
    }

    fun closeMenu() {
        _state.value = _state.value.copy(
            menuExpanded = false
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
} 