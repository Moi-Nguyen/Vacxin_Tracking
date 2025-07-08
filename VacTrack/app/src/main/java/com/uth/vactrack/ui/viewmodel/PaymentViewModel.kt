package com.uth.vactrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PaymentState(
    val selectedPaymentMethod: String = "Credit Card",
    val cardNumber: String = "",
    val cardHolderName: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val success: Boolean = false
)

class PaymentViewModel : ViewModel() {
    private val _state = MutableStateFlow(PaymentState())
    val state: StateFlow<PaymentState> = _state.asStateFlow()

    fun updatePaymentMethod(method: String) {
        _state.value = _state.value.copy(selectedPaymentMethod = method)
    }

    fun updateCardNumber(cardNumber: String) {
        _state.value = _state.value.copy(cardNumber = cardNumber)
    }

    fun updateCardHolderName(name: String) {
        _state.value = _state.value.copy(cardHolderName = name)
    }

    fun updateExpiryDate(date: String) {
        _state.value = _state.value.copy(expiryDate = date)
    }

    fun updateCvv(cvv: String) {
        _state.value = _state.value.copy(cvv = cvv)
    }

    fun isFormValid(): Boolean {
        val currentState = _state.value
        return when (currentState.selectedPaymentMethod) {
            "Credit Card" -> {
                currentState.cardNumber.isNotBlank() &&
                currentState.cardHolderName.isNotBlank() &&
                currentState.expiryDate.isNotBlank() &&
                currentState.cvv.isNotBlank()
            }
            "Cash" -> true
            else -> false
        }
    }

    fun processPayment() {
        if (!isFormValid()) {
            _state.value = _state.value.copy(error = "Vui lòng điền đầy đủ thông tin")
            return
        }

        _state.value = _state.value.copy(
            isLoading = true,
            error = null,
            message = null,
            success = false
        )

        viewModelScope.launch {
            try {
                // TODO: Implement actual payment processing
                kotlinx.coroutines.delay(2000) // Simulate payment processing
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    message = "Thanh toán thành công",
                    success = true
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Thanh toán thất bại",
                    success = false
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun clearMessage() {
        _state.value = _state.value.copy(message = null)
    }

    fun resetSuccess() {
        _state.value = _state.value.copy(success = false)
    }
} 