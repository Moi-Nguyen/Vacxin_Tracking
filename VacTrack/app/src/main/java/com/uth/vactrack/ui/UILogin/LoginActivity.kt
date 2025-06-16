package com.uth.vactrack.ui.UILogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uth.vactrack.ui.theme.VacTrackTheme
import com.uth.vactrack.config.SessionManager
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VacTrackTheme {
                LoginScreen(
                    onLoginSuccess = { userId ->
                        // Start session when login is successful
                        SessionManager(this).startSession()
                        // Navigate to main screen
                        // TODO: Add navigation to main screen
                    }
                )
            }
        }
    }
} 