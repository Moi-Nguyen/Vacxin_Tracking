package com.uth.vactrack

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uth.vactrack.ui.theme.VacTrackTheme
import com.uth.vactrack.ui.UILogin.AppNavHost

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Nhận màn hình khởi đầu từ intent
        val startDestination = intent.getStringExtra("navigateTo") ?: "login"

        setContent {
            VacTrackTheme {
                AppNavHost(startDestination = startDestination)
            }
        }
    }
}
