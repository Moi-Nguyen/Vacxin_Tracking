package com.uth.vactrack.ui.UIUser

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun UserNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController = navController)
        }

        composable("appointment") {
            AppointmentScreen(navController = navController, onBack = { navController.popBackStack() })
        }

        composable("select_service") {
            SelectServiceScreen()
        }
    }
}
