package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Booking(
    val name: String,
    val service: String,
    val facility: String,
    val date: String,
    val time: String
)

data class TrackingBookingState(
    val currentBookings: List<Booking> = emptyList(),
    val historyBookings: List<Booking> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TrackingBookingViewModel : ViewModel() {
    private val _state = MutableStateFlow(TrackingBookingState())
    val state: StateFlow<TrackingBookingState> = _state.asStateFlow()

    init {
        loadBookings()
    }

    private fun loadBookings() {
        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )

        // Mock data - in real app, this would come from repository
        val currentBookings = listOf(
            Booking("Nguyen Van A", "Vaccine Services", "Gia Dinh Hospital", "13/05/2025", "8:00 AM"),
            Booking("Nguyen Van A", "HPV Vaccine", "City Hospital", "15/05/2025", "9:00 AM")
        )

        val historyBookings = listOf(
            Booking("Nguyen Van A", "Vaccine Services", "Gia Dinh Hospital", "10/04/2025", "10:00 AM"),
            Booking("Nguyen Van A", "COVID-19 Vaccine", "City Hospital", "01/03/2025", "2:00 PM")
        )

        _state.value = _state.value.copy(
            currentBookings = currentBookings,
            historyBookings = historyBookings,
            isLoading = false
        )
    }

    fun refreshBookings() {
        loadBookings()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
} 