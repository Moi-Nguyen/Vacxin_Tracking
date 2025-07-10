package com.uth.vactrack.data.repository

import com.uth.vactrack.data.model.Service
import com.uth.vactrack.R

class ServiceRepository {
    
    fun getServices(): List<Service> {
        return listOf(
            Service(
                id = "6830b20280eba4e950c8a161",
                name = "Vaccine Services",
                description = "Comprehensive vaccination programs for both children and adults, including routine immunizations and specialized vaccines.",
                price = 100,
                icon = R.drawable.ic_service_vaccination
            ),
            Service(
                id = "6830b20280eba4e950c8a161",
                name = "Outpatient Vaccination",
                description = "Routine vaccinations, booster shots, and consultations with specialists for vaccine-related queries and concerns.",
                price = 200,
                icon = R.drawable.ic_service_consultation
            ),
            Service(
                id = "6830b20280eba4e950c8a161",
                name = "Emergency Vaccine Services",
                description = "Immediate vaccination for travel-related diseases, outbreak control, and emergency vaccination in case of exposure to infectious diseases.",
                price = 300,
                icon = R.drawable.ic_service_screening
            ),
            Service(
                id = "6830b20280eba4e950c8a161",
                name = "Vaccine Administration",
                description = "Professional administration of various vaccines, including seasonal flu shots, hepatitis, and HPV vaccines, performed by trained healthcare professionals.",
                price = 50,
                icon = R.drawable.ic_service_tracking
            )
        )
    }

    fun getServiceById(id: String): Service? {
        return getServices().find { it.id == id }
    }
} 