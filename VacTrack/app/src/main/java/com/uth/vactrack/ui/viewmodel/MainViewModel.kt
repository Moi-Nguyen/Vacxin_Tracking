package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val features: List<Feature> = listOf(
        Feature("Book an Appointment", "Fast and easy booking", "appointment"),
        Feature("Locate Vaccine Facility", "Bệnh viện Gia Định", "location"),
        Feature("Request an Emergency", "Immediate support", "emergency")
    ),
    val appointments: List<Appointment> = listOf(
        Appointment("General Hospital", "HPV, HIB Vaccine", "12/05/2025", "8:00 AM - 9:00 AM"),
        Appointment("City Hospital", "COVID-19 Vaccine", "14/05/2025", "10:00 AM - 11:00 AM")
    ),
    val services: List<Service> = listOf(
        Service("Vaccination", "vaccination"),
        Service("Screening", "screening"),
        Service("Tracking", "tracking"),
        Service("Consultation", "consultation")
    )
)

data class Feature(
    val title: String,
    val subtitle: String,
    val action: String
)

data class Appointment(
    val facility: String,
    val service: String,
    val date: String,
    val time: String
)

data class Service(
    val name: String,
    val action: String
)

class MainViewModel : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    fun setLoading(isLoading: Boolean) {
        _state.value = _state.value.copy(isLoading = isLoading)
    }

    fun setError(error: String?) {
        _state.value = _state.value.copy(error = error)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun loadAppointments() {
        // TODO: Load appointments from repository
        setLoading(true)
        // Simulate API call
        setLoading(false)
    }

    fun loadServices() {
        // TODO: Load services from repository
        setLoading(true)
        // Simulate API call
        setLoading(false)
    }
} 