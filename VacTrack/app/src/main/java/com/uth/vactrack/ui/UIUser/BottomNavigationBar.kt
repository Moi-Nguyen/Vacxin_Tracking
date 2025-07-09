package com.uth.vactrack.ui.UIUser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uth.vactrack.R

@Composable
fun BottomNavigationBar(
    selectedIndex: Int = 0,
    onRecordClick: () -> Unit = {}
) {
    val icons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_record,
        R.drawable.ic_log,
        R.drawable.ic_user
    )
    val items = listOf("Home", "Record", "Log", "Profile")

    Box(modifier = Modifier.height(80.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 5.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icons.forEachIndexed { index, icon ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        if (items[index] == "Record") onRecordClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = items[index],
                        tint = if (index == selectedIndex) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                if (index == selectedIndex) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
                                shape = CircleShape
                            )
                            .padding(4.dp)
                    )
                    Text(
                        text = items[index],
                        fontSize = 14.sp,
                        color = if (index == selectedIndex) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onRecordClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-28).dp)
                .size(56.dp),
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
} 