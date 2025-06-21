package com.uth.vactrack.ui.UILogin

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uth.vactrack.ui.UIUser.HomeScreen
import com.uth.vactrack.ui.UIUser.MainScreen

@Composable
fun AppNavHost(startDestination: String = "login") {
    val navController = rememberNavController()
    var loginEmail by remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = startDestination) {
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
                onResend = {
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

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

        // ✅ Thêm HomeScreen sau login
        composable("home") {
            HomeScreen(
                onLearnMoreClick = {
                    navController.navigate("main")
                }
            )
        }

        // ✅ Thêm MainScreen sau khi nhấn Learn More
        composable("main") {
            MainScreen()
        }
    }
}
