package com.uth.vactrack.ui.doctor

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLDecoder

@Composable
fun DoctorNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "doctor_home") {

        // Trang chủ
        composable("doctor_home") {
            DoctorHomeScreen(navController = navController, token = "Bearer your_token_here")
        }

        // Danh sách lịch hẹn
        composable("appointment_list") {
            AppointmentListScreen(navController, navController.currentBackStackEntry!!)
        }

        // Chi tiết cuộc hẹn
        composable(
            "appointment_detail/{appointmentJson}",
            arguments = listOf(navArgument("appointmentJson") { type = NavType.StringType })
        ) { backStackEntry ->
            AppointmentDetailScreen(navController, backStackEntry)
        }

        // Chat: danh sách bệnh nhân
        composable("chat_screen") {
            DoctorOnlineConsultationScreen(navController)
        }

        // Chat chi tiết với bệnh nhân
        composable(
            "chat_detail/{id}/{name}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            DoctorChatDetailScreen(navController, name, id)
        }

        // Lịch sử bệnh nhân
        composable("patients_history") {
            PatientHistoryScreen(navController)
        }

        // Lịch sử chi tiết toàn bộ
        composable(
            "patients_history_detail/{patientsJson}",
            arguments = listOf(navArgument("patientsJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientsJson = backStackEntry.arguments?.getString("patientsJson")
            PatientHistoryDetailScreen(navController, patientsJson)
        }

        // Hồ sơ bệnh nhân cụ thể
        composable(
            "patient_info/{name}/{info}/{lastVisit}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("info") { type = NavType.StringType },
                navArgument("lastVisit") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = URLDecoder.decode(backStackEntry.arguments?.getString("name") ?: "", "UTF-8")
            val info = URLDecoder.decode(backStackEntry.arguments?.getString("info") ?: "", "UTF-8")
            val lastVisit = URLDecoder.decode(backStackEntry.arguments?.getString("lastVisit") ?: "", "UTF-8")
            PatientInfoScreen(navController, name, info, lastVisit)
        }

        // Cài đặt
        composable("settings_screen") {
            SettingsScreen(navController)
        }
    }
}
