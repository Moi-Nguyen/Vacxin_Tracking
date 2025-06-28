package com.uth.vactrack.ui.UIUser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uth.vactrack.R

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    onLearnMoreClick: () -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        // ===== Top logo + avatar =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 10.dp, end = 10.dp, bottom = 12.dp)
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_logo_xoanen),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_avatar),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("profile")
                    }
            )
        }

        // ===== VACTRACK title bar =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color(0xFF2D25C9))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "VACTRACK",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier
                        .size(45.dp)
                        .padding(end = 10.dp)
                )
                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { menuExpanded = true }
                    )

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier
                            .background(Color(0xFF2D25C9), shape = RoundedCornerShape(8.dp))
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Feedback", color = Color.White, fontSize = 14.sp)
                            },
                            onClick = {
                                menuExpanded = false
                                navController.navigate("feedback")
                            },
                            colors = MenuDefaults.itemColors(textColor = Color.White)
                        )
                        DropdownMenuItem(
                            text = {
                                Text("Logout", color = Color.White, fontSize = 14.sp)
                            },
                            onClick = {
                                menuExpanded = false
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            colors = MenuDefaults.itemColors(textColor = Color.White)
                        )
                    }
                }
            }
        }

        // ===== Doctor banner (Our Services) =====
        Image(
            painter = painterResource(id = R.drawable.ic_img_doctor),
            contentDescription = "Doctor Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(top = 10.dp, start = 16.dp, end = 16.dp)
        )

        // ===== Seminar banner =====
        Image(
            painter = painterResource(id = R.drawable.ic_img_seminar),
            contentDescription = "Seminar Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        // ===== Learn More =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clickable { onLearnMoreClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Learn More",
                color = Color(0xFF1A237E),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(4.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_move),
                contentDescription = "Arrow",
                tint = Color(0xFF1A237E),
                modifier = Modifier
                    .size(25.dp)
                    .padding(start = 4.dp)
            )
        }

        // ===== Info Section =====
        Image(
            painter = painterResource(id = R.drawable.ic_img_info_footer),
            contentDescription = "Info Footer",
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(vertical = 16.dp, horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
