package com.uth.vactrack.ui.UILogin

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uth.vactrack.ui.doctor.* // ✅ THÊM: để gọi DoctorHomeScreen
import androidx.compose.foundation.BorderStroke

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    var loginEmail by remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = "doctor_home") {

        /* ✅ PHẦN LOGIN GỐC GIỮ NGUYÊN - KHÔNG ĐỤNG */
        /*composable("login") {
            LoginScreen(
                onLoginSuccess = { *//* TODO: Điều hướng sang màn khác *//* },
                onSignUpClick = { navController.navigate("register") },
                onForgotPassword = { email ->
                    navController.navigate("forgot_password?email=$email")
                }
            )
        }*/
        composable("login") {
            LoginScreen(
                onLoginSuccess = { _ ->
                    navController.navigate("doctor_home") {
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
            arguments = listOf(navArgument("email") { type = NavType.StringType; defaultValue = "" })
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
            arguments = listOf(navArgument("email") { type = NavType.StringType; defaultValue = "" })
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

        composable(
            "confirm_reset?resetToken={resetToken}",
            arguments = listOf(navArgument("resetToken") { type = NavType.StringType; defaultValue = "" })
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
            arguments = listOf(navArgument("resetToken") { type = NavType.StringType; defaultValue = "" })
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

        // ✅ ✅ ✅ THÊM MỚI KHÔNG ĐỤNG CODE CŨ ✅ ✅ ✅

        composable(
            route = "doctor_home?token={token}",
            arguments = listOf(navArgument("token") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            DoctorHomeScreen(navController = navController, token = token)
        }

        composable("patients_history") {
            PatientHistoryScreen(navController = navController)
        }

        // Thêm route cho màn hình PatientInfoScreen
        composable(
            "patient_info/{name}/{info}/{lastVisit}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("info") { type = NavType.StringType },
                navArgument("lastVisit") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val info = backStackEntry.arguments?.getString("info") ?: ""
            val lastVisit = backStackEntry.arguments?.getString("lastVisit") ?: ""
            PatientInfoScreen(navController = navController, name = name, info = info, lastVisit = lastVisit)
        }

        composable(
            "patients_history_detail/{patientsJson}",
            arguments = listOf(navArgument("patientsJson") { type = NavType.StringType })
        ) { backStackEntry ->
            PatientHistoryDetailScreen(
                navController = navController,
                patientsJson = backStackEntry.arguments?.getString("patientsJson")
            )
        }

        composable("appointment_list") { backStackEntry ->
            AppointmentListScreen(
                navController = navController,
                backStackEntry = backStackEntry
            )
        }
        composable(
            "appointment_detail/{appointmentJson}",
            arguments = listOf(navArgument("appointmentJson") { type = NavType.StringType })
        ) { backStackEntry ->
            AppointmentDetailScreen(navController, backStackEntry)
        }

        composable("settings_screen") {
            SettingsScreen(navController = navController)
        }

        // Add chat_screen route for chat icon in BottomNavBar
        composable("chat_screen") {
            DoctorOnlineConsultationScreen(navController = navController)
        }

        // Add chat_detail route for chat detail screen
        composable(
            route = "chat_detail/{patientId}/{patientName}",
            arguments = listOf(
                navArgument("patientId") { type = NavType.StringType },
                navArgument("patientName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            val patientName = backStackEntry.arguments?.getString("patientName") ?: ""
            DoctorChatDetailScreen(navController = navController, patientName = patientName, patientId = patientId)
        }

    }
}


