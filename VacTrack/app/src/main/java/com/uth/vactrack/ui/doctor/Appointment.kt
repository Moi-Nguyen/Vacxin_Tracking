package com.uth.vactrack.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Appointment(
    val name: String,
    val age: Int,
    val hasInsurance: Boolean,
    val date: String,
    val time: String,
    val reason: String,
    val status: String
) : Parcelable
