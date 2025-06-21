package com.uth.vactrack.ui.UIUser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uth.vactrack.ui.theme.VacTrackTheme

class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VacTrackTheme {
                UserNavHost()
            }
        }
    }
}
