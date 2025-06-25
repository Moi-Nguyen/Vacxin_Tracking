package com.uth.vactrack.ui.UILogin

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.uth.vactrack.ui.UIUser.*

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

        // Main screen
        composable("main") {
            MainScreen(navController = navController)
        }

        // Appointment screen
        composable("appointment") {
            AppointmentScreen(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        // Select Service screen ✅ fixed onBack
        composable("select_service") {
            SelectServiceScreen(onBack = { navController.popBackStack() })
        }
    }
}
