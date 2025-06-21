package com.uth.vactrack.ui.UIUser

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uth.vactrack.ui.UIUser.MainScreen

@Composable
fun UserNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen()
        }

        // Nếu cần thêm các màn khác:
        // composable("record") { RecordScreen() }
        // composable("profile") { ProfileScreen() }
    }
}
