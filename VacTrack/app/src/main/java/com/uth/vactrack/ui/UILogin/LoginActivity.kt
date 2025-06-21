package com.uth.vactrack.ui.UILogin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uth.vactrack.MainActivity
import com.uth.vactrack.config.SessionManager
import com.uth.vactrack.ui.theme.VacTrackTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VacTrackTheme {
                LoginScreen(
                    onLoginSuccess = { _ ->
                        // Start session when login is successful
                        SessionManager(this).startSession()
                        // Navigate to MainActivity
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("navigateTo", "home") // ✅ Thêm dòng này
                        startActivity(intent)

                        // Finish login activity to prevent going back
                        finish()
                    }
                )
            }
        }
    }
}
