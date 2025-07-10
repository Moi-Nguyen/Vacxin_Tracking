package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uth.vactrack.data.model.Appointment
import com.uth.vactrack.data.model.BookingDetail
import com.uth.vactrack.data.repository.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeState(
    val menuExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val appointments: List<Appointment> = emptyList(),
    val bookings: List<BookingDetail> = emptyList()
)

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    
    private val appointmentRepository = AppointmentRepository()

    fun loadAppointments(userId: String, token: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                // Booking history (API mới)
                val bookingResult = appointmentRepository.getBookingHistory(token, userId)
                if (bookingResult.isSuccess) {
                    val bookings = bookingResult.getOrNull() ?: emptyList()
                    _state.value = _state.value.copy(
                        bookings = bookings,
                        isLoading = false
                    )
                } else {
                    // Fallback: thử lấy appointments cũ
                    val appointmentResult = appointmentRepository.getAppointmentsByUserId(userId)
                    if (appointmentResult.isSuccess) {
                        val appointments = appointmentResult.getOrNull() ?: emptyList()
                        _state.value = _state.value.copy(
                            appointments = appointments,
                            isLoading = false
                        )
                    } else {
                        _state.value = _state.value.copy(
                            error = "Không thể tải lịch hẹn",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Không thể tải lịch hẹn",
                    isLoading = false
                )
            }
        }
    }

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