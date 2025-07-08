package com.uth.vactrack

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uth.vactrack.ui.theme.VacTrackTheme
import com.uth.vactrack.ui.UILogin.AppNavHostMVVM
import com.uth.vactrack.ui.viewmodel.SharedViewModel

class MainActivityMVVM : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startDestination = intent.getStringExtra("navigateTo") ?: "login"

        setContent {
            val sharedViewModel: SharedViewModel = viewModel()
            val sharedState by sharedViewModel.sharedState.collectAsStateWithLifecycle()

            VacTrackTheme(darkTheme = sharedState.isDarkTheme) {
                AppNavHostMVVM(
                    startDestination = startDestination
                )
            }
        }
    }
} 