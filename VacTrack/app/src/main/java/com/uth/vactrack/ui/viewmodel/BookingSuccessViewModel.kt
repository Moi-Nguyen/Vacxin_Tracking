package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BookingSuccessState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

class BookingSuccessViewModel : ViewModel() {
    private val _state = MutableStateFlow(BookingSuccessState())
    val state: StateFlow<BookingSuccessState> = _state.asStateFlow()

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun clearMessage() {
        _state.value = _state.value.copy(message = null)
    }
} 