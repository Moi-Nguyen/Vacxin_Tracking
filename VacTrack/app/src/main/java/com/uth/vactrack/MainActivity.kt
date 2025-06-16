package com.uth.vactrack

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.FirebaseApp
import com.uth.vactrack.ui.theme.VacTrackTheme
import com.uth.vactrack.ui.UILogin.AppNavHost

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VacTrackTheme {
                AppNavHost()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VacTrackTheme {
        AppNavHost()
    }
}

