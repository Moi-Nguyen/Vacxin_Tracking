package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.uth.vactrack.data.model.Service
import com.uth.vactrack.data.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ServiceState(
    val services: List<Service> = emptyList(),
    val selectedService: Service? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ServiceViewModel : ViewModel() {
    private val serviceRepository = ServiceRepository()
    
    private val _serviceState = MutableStateFlow(ServiceState())
    val serviceState: StateFlow<ServiceState> = _serviceState.asStateFlow()

    init {
        loadServices()
    }

    private fun loadServices() {
        _serviceState.value = _serviceState.value.copy(
            isLoading = true
        )
        
        try {
            val services = serviceRepository.getServices()
            _serviceState.value = _serviceState.value.copy(
                services = services,
                isLoading = false
            )
        } catch (e: Exception) {
            _serviceState.value = _serviceState.value.copy(
                isLoading = false,
                error = e.message ?: "Failed to load services"
            )
        }
    }

    fun selectService(service: Service) {
        _serviceState.value = _serviceState.value.copy(
            selectedService = service
        )
    }

    fun getServiceById(id: String): Service? {
        return serviceRepository.getServiceById(id)
    }

    fun clearError() {
        _serviceState.value = _serviceState.value.copy(error = null)
    }
} 