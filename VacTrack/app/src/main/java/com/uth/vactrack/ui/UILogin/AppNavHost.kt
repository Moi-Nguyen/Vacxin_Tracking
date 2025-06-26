package com.uth.vactrack.ui.UILogin

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.uth.vactrack.ui.UIUser.*
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavHost(startDestination: String = "login") {
    val navController = rememberNavController()
    var loginEmail by remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = startDestination) {

        // Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate("register") },
                onForgotPassword = { email ->
                    navController.navigate("forgot_password?email=$email")
                }
            )
        }

        // Register
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Forgot Password
        composable(
            "forgot_password?email={email}",
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ForgotPasswordScreen(
                initialEmail = email,
                onBack = { navController.popBackStack() },
                onRequestResetSuccess = { sentEmail ->
                    navController.navigate("otp?email=$sentEmail")
                }
            )
        }

        // OTP
        composable(
            "otp?email={email}",
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            OtpScreen(
                email = email,
                onOtpVerified = { resetToken ->
                    navController.navigate("confirm_reset?resetToken=$resetToken")
                },
                onResend = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // Confirm Reset
        composable(
            "confirm_reset?resetToken={resetToken}",
            arguments = listOf(navArgument("resetToken") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val resetToken = backStackEntry.arguments?.getString("resetToken") ?: ""
            ConfirmResetScreen(
                onConfirm = {
                    navController.navigate("set_new_password?resetToken=$resetToken")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Set New Password
        composable(
            "set_new_password?resetToken={resetToken}",
            arguments = listOf(navArgument("resetToken") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val resetToken = backStackEntry.arguments?.getString("resetToken") ?: ""
            SetNewPasswordScreen(
                resetToken = resetToken,
                onPasswordReset = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Home
        composable("home") {
            HomeScreen(
                onLearnMoreClick = {
                    navController.navigate("main")
                }
            )
        }

        // Main
        composable("main") {
            MainScreen(navController = navController)
        }

        // Appointment
        composable("appointment") {
            AppointmentScreen(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        // Select Service
        composable("select_service") {
            SelectServiceScreen(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        // Select Time and Slot
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
                onBack = { navController.popBackStack() }
            )
        }

        // Payment screen
        composable(
            "payment/{service}/{date}/{time}/{bill}",
            arguments = listOf(
                navArgument("service") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType },
                navArgument("bill") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val service = URLDecoder.decode(
                backStackEntry.arguments?.getString("service") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val date = URLDecoder.decode(
                backStackEntry.arguments?.getString("date") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val time = URLDecoder.decode(
                backStackEntry.arguments?.getString("time") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val bill = backStackEntry.arguments?.getInt("bill") ?: 0

            PaymentScreen(
                serviceName = service,
                selectedDate = date,
                selectedTime = time,
                bill = bill,
                onBack = { navController.popBackStack() },
                onCancel = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                onPay = {
                    val encodedService = URLEncoder.encode(service, StandardCharsets.UTF_8.toString())
                    val encodedDate = URLEncoder.encode(date, StandardCharsets.UTF_8.toString())
                    val encodedTime = URLEncoder.encode(time, StandardCharsets.UTF_8.toString())
                    navController.navigate("booking_success/$encodedService/$encodedDate/$encodedTime/$bill")
                }
            )
        }

        // Booking Success ✅
        composable(
            "booking_success/{service}/{date}/{time}/{bill}",
            arguments = listOf(
                navArgument("service") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType },
                navArgument("bill") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val service = URLDecoder.decode(
                backStackEntry.arguments?.getString("service") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val date = URLDecoder.decode(
                backStackEntry.arguments?.getString("date") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val time = URLDecoder.decode(
                backStackEntry.arguments?.getString("time") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val bill = backStackEntry.arguments?.getInt("bill") ?: 0

            BookingSuccessScreen(
                serviceName = service,
                selectedDate = date,
                selectedTime = time,
                bill = bill,
                onFinish = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}
