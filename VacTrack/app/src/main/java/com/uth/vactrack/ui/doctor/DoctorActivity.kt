package com.uth.vactrack.ui.doctor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.navigation.compose.rememberNavController
import com.uth.vactrack.ui.theme.VacTrackTheme

class DoctorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VacTrackTheme {
                val navController = rememberNavController()
                DoctorNavHost(navController)
            }
        }
    }
}
