package com.uth.vactrack.ui.UIUser

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uth.vactrack.R
import com.uth.vactrack.ui.viewmodel.EditProfileViewModel
import com.uth.vactrack.ui.viewmodel.SharedViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    onBack: () -> Unit = {},
    onSubmit: () -> Unit = {},
    editProfileViewModel: EditProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by editProfileViewModel.state.collectAsStateWithLifecycle()
    
    val genderOptions = listOf("Male", "Female", "Other")
    val blue = Color(0xFF1976D2)

    // Handle success
    LaunchedEffect(state.success) {
        if (state.success) {
            Toast.makeText(context, state.message ?: "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            editProfileViewModel.resetSuccess()
            onSubmit()
        }
    }

    // Handle error
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            editProfileViewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = { Spacer(modifier = Modifier.width(48.dp)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.fullName,
                onValueChange = { editProfileViewModel.updateFullName(it) },
                label = { Text("Full name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.nickName,
                onValueChange = { editProfileViewModel.updateNickName(it) },
                label = { Text("Nick name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { editProfileViewModel.updateEmail(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_flag_vietnam),
                    contentDescription = "Flag Vietnam",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = state.phone,
                    onValueChange = { editProfileViewModel.updatePhone(it) },
                    label = { Text("Phone number") },
                    modifier = Modifier.weight(1f)
                )
            }

            ExposedDropdownMenuBox(
                expanded = state.expandedGender,
                onExpandedChange = { editProfileViewModel.toggleGenderDropdown() }
            ) {
                OutlinedTextField(
                    value = state.selectedGender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = state.expandedGender,
                    onDismissRequest = { editProfileViewModel.toggleGenderDropdown() }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                editProfileViewModel.updateGender(option)
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.address,
                onValueChange = { editProfileViewModel.updateAddress(it) },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { editProfileViewModel.updateProfile() },
                enabled = editProfileViewModel.isFormValid() && !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (editProfileViewModel.isFormValid() && !state.isLoading) blue else Color.Gray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("SUBMIT", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
} 