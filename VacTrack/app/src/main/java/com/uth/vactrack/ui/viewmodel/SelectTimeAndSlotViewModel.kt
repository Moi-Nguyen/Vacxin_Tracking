package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SelectTimeAndSlotState(
    val selectedDate: String = "",
    val selectedTime: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val availableSlots: Map<String, List<String>> = mapOf(
        "Tuesday, 13 May 2025" to listOf("8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM"),
        "Wednesday, 14 May 2025" to listOf("8:30 AM", "9:30 AM", "10:30 AM", "11:30 AM"),
        "Thursday, 15 May 2025" to listOf("8:00 AM", "9:00 AM", "10:00 AM")
    )
)

class SelectTimeAndSlotViewModel : ViewModel() {
    private val _state = MutableStateFlow(SelectTimeAndSlotState())
    val state: StateFlow<SelectTimeAndSlotState> = _state.asStateFlow()

    fun selectDate(date: String) {
        _state.value = _state.value.copy(selectedDate = date)
    }

    fun selectTime(time: String) {
        _state.value = _state.value.copy(selectedTime = time)
    }

    fun clearSelection() {
        _state.value = _state.value.copy(
            selectedDate = "",
            selectedTime = ""
        )
    }

    fun setError(error: String?) {
        _state.value = _state.value.copy(error = error)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun setLoading(isLoading: Boolean) {
        _state.value = _state.value.copy(isLoading = isLoading)
    }
} 