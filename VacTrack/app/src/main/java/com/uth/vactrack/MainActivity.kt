package com.uth.vactrack

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.uth.vactrack.ui.theme.VacTrackTheme
import com.uth.vactrack.ui.UILogin.AppNavHost

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startDestination = intent.getStringExtra("navigateTo") ?: "login"

        setContent {
            var darkTheme by remember { mutableStateOf(false) }

            VacTrackTheme(darkTheme = darkTheme) {
                AppNavHost(
                    startDestination = startDestination,
                    isDarkTheme = darkTheme,
                    onToggleTheme = { darkTheme = !darkTheme }
                )
            }
        }
    }
}
