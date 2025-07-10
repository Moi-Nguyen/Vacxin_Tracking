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
                onSignUpClick = { navController.navigate("register") },
                onForgotPassword = { email -> navController.navigate("forgot_password") },
                sharedViewModel = sharedViewModel
            )
        }
        composable("register") {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                sharedViewModel = sharedViewModel
            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onRequestResetSuccess = { email -> 
                    val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString())
                    navController.navigate("otp/$encodedEmail")
                }
            )
        }
        composable(
            "otp/{email}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8.toString())
            OtpScreen(
                email = decodedEmail,
                onOtpVerified = { resetToken -> 
                    val encodedToken = URLEncoder.encode(resetToken, StandardCharsets.UTF_8.toString())
                    navController.navigate("set_new_password/$encodedToken")
                },
                onResend = { /* TODO: Handle resend */ },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "set_new_password/{resetToken}",
            arguments = listOf(
                navArgument("resetToken") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val resetToken = backStackEntry.arguments?.getString("resetToken") ?: ""
            val decodedToken = URLDecoder.decode(resetToken, StandardCharsets.UTF_8.toString())
            SetNewPasswordScreen(
                resetToken = decodedToken,
                onPasswordReset = { navController.navigate("login") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("profile") {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                navController = navController, 
                sharedViewModel = sharedViewModel
            )
        }
        composable("edit_profile") {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                navController = navController, 
                sharedViewModel = sharedViewModel
            )
        }
        composable("home") {
            HomeScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable("appointment") {
            AppointmentScreen(
                onBack = { navController.popBackStack() },
                navController = navController, 
                sharedViewModel = sharedViewModel
            )
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
                onBack = { navController.popBackStack() },
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
                onBack = { navController.popBackStack() },
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
                onBack = { navController.popBackStack() },
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        composable("select_service") {
            SelectServiceScreen(
                onBack = { navController.popBackStack() },
                navController = navController, 
                sharedViewModel = sharedViewModel
            )
        }
        composable("tracking_booking") {
            TrackingBookingScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable("main") {
            MainScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
    }
} 