package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.uth.vactrack.data.model.BookingRequest
import com.uth.vactrack.data.repository.AppointmentRepository
import kotlinx.coroutines.launch

data class AppointmentState(
    val name: String = "",
    val birthday: String = "",
    val phone: String = "",
    val insuranceId: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

class AppointmentViewModel : ViewModel() {
    private val _state = MutableStateFlow(AppointmentState())
    val state: StateFlow<AppointmentState> = _state.asStateFlow()

    private val repository = AppointmentRepository()

    fun updateName(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun updateBirthday(birthday: String) {
        _state.value = _state.value.copy(birthday = birthday)
    }

    fun updatePhone(phone: String) {
        _state.value = _state.value.copy(phone = phone)
    }

    fun updateInsuranceId(insuranceId: String) {
        _state.value = _state.value.copy(insuranceId = insuranceId)
    }

    fun isBirthdayValid(): Boolean {
        return _state.value.birthday.matches(
            Regex("""^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\d{4})$""")
        )
    }

    fun isPhoneValid(): Boolean {
        val phone = _state.value.phone
        return phone.length in 10..12 && phone.all { it.isDigit() }
    }

    fun isFormValid(): Boolean {
        val currentState = _state.value
        return currentState.name.isNotBlank() &&
                currentState.birthday.isNotBlank() && isBirthdayValid() &&
                currentState.phone.isNotBlank() && isPhoneValid() &&
                currentState.insuranceId.isNotBlank()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun clearMessage() {
        _state.value = _state.value.copy(message = null)
    }

    fun bookAppointment(
        token: String,
        userId: String,
        serviceId: String,
        facilityId: String,
        date: String,
        time: String
    ) {
        _state.value = _state.value.copy(isLoading = true, error = null, message = null)
        viewModelScope.launch {
            val result = repository.bookAppointmentWithApi(
                token,
                BookingRequest(
                    userId = userId,
                    serviceId = serviceId,
                    facilityId = facilityId,
                    date = date,
                    time = time
                )
            )
            _state.value = if (result.isSuccess) {
                _state.value.copy(isLoading = false, message = result.getOrNull()?.message)
            } else {
                _state.value.copy(isLoading = false, error = result.exceptionOrNull()?.message ?: "Booking failed")
            }
        }
    }
} 