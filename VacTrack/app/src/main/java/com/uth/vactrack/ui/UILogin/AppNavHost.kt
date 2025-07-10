package com.uth.vactrack.ui.UILogin

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.uth.vactrack.ui.UIUser.*
import com.uth.vactrack.ui.viewmodel.SharedViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    sharedViewModel: SharedViewModel = viewModel()
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") },
                sharedViewModel = sharedViewModel
            )
        }
        composable("register") {
            RegisterScreen(sharedViewModel = sharedViewModel)
        }
        composable("profile") {
            ProfileScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable("edit_profile") {
            EditProfileScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable("home") {
            HomeScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable("appointment") {
            AppointmentScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable(
            "booking_success/{serviceName}/{selectedDate}/{selectedTime}/{bill}",
            arguments = listOf(
                navArgument("serviceName") { type = NavType.StringType },
                navArgument("selectedDate") { type = NavType.StringType },
                navArgument("selectedTime") { type = NavType.StringType },
                navArgument("bill") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
            val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""
            val selectedTime = backStackEntry.arguments?.getString("selectedTime") ?: ""
            val bill = backStackEntry.arguments?.getInt("bill") ?: 0
            BookingSuccessScreen(
                serviceName = serviceName,
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                bill = bill,
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        composable(
            "payment/{serviceName}/{selectedDate}/{selectedTime}/{bill}",
            arguments = listOf(
                navArgument("serviceName") { type = NavType.StringType },
                navArgument("selectedDate") { type = NavType.StringType },
                navArgument("selectedTime") { type = NavType.StringType },
                navArgument("bill") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
            val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""
            val selectedTime = backStackEntry.arguments?.getString("selectedTime") ?: ""
            val bill = backStackEntry.arguments?.getInt("bill") ?: 0
            PaymentScreen(
                serviceName = serviceName,
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                bill = bill,
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        composable(
            "select_time_and_slot/{serviceName}/{bill}",
            arguments = listOf(
                navArgument("serviceName") { type = NavType.StringType },
                navArgument("bill") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
            val bill = backStackEntry.arguments?.getInt("bill") ?: 0
            SelectTimeAndSlotScreen(
                serviceName = serviceName,
                bill = bill,
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        composable("select_service") {
            SelectServiceScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable("tracking_booking") {
            TrackingBookingScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable("main") {
            MainScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
    }
} 