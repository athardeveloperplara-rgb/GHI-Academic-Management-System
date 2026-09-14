package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HospCrimson
import com.example.ui.theme.HospGoldAccent
import com.example.ui.theme.HospGoldBright
import com.example.ui.theme.HospNavyDark
import com.example.ui.theme.HospNavyPrimary
import com.example.ui.viewmodel.AppScreen

data class NavItem(
    val screen: AppScreen,
    val icon: ImageVector,
    val label: String
)

val primaryNavItems = listOf(
    NavItem(AppScreen.DASHBOARD, Icons.Default.Dashboard, "Dashboard"),
    NavItem(AppScreen.DOSEN, Icons.Default.Person, "Dosen"),
    NavItem(AppScreen.SISWA, Icons.Default.People, "Siswa"),
    NavItem(AppScreen.PELAJARAN, Icons.Default.Book, "Pelajaran"),
    NavItem(AppScreen.NILAI, Icons.Default.Grade, "Nilai"),
    NavItem(AppScreen.LAPORAN, Icons.Default.Print, "Laporan"),
    NavItem(AppScreen.NOTIFIKASI, Icons.Default.Notifications, "Pengingat"),
    NavItem(AppScreen.ANALITIK, Icons.Default.Analytics, "Analitik")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    currentScreen: AppScreen,
    staffName: String,
    pendingNotificationCount: Int,
    onNotificationClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier.testTag("app_header_bar"),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = HospNavyPrimary,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(HospGoldAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Grand Hospitality Logo",
                        tint = HospNavyDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "GRAND HOSPITALITY",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = HospGoldBright
                        )
                    )
                    Text(
                        text = "LKP Institute • ${currentScreen.label}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.testTag("nav_notification_bell")
            ) {
                BadgedBox(
                    badge = {
                        if (pendingNotificationCount > 0) {
                            Badge(
                                containerColor = HospCrimson,
                                contentColor = Color.White
                            ) {
                                Text("$pendingNotificationCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Pengingat",
                        tint = if (pendingNotificationCount > 0) HospGoldBright else Color.White
                    )
                }
            }

            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.testTag("staff_profile_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(HospGoldAccent.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ADM",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = HospGoldBright
                            )
                        )
                    }
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(staffName, fontWeight = FontWeight.SemiBold)
                                Text(
                                    "Staf Administrasi Akademik",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        },
                        onClick = { },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Keluar (Logout)", color = HospCrimson) },
                        onClick = {
                            showMenu = false
                            onLogoutClick()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Logout, contentDescription = null, tint = HospCrimson)
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun AppBottomBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    pendingNotificationCount: Int,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("app_bottom_nav"),
        containerColor = HospNavyPrimary,
        tonalElevation = 8.dp
    ) {
        primaryNavItems.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationBarItem(
                modifier = Modifier.testTag("nav_item_${item.screen.name.lowercase()}"),
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    if (item.screen == AppScreen.NOTIFIKASI && pendingNotificationCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = HospCrimson) {
                                    Text("$pendingNotificationCount")
                                }
                            }
                        ) {
                            Icon(item.icon, contentDescription = item.label)
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.label)
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        maxLines = 1,
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = HospNavyDark,
                    selectedTextColor = HospGoldBright,
                    indicatorColor = HospGoldAccent,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
fun AppNavRail(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    pendingNotificationCount: Int,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier
            .fillMaxHeight()
            .testTag("app_nav_rail"),
        containerColor = HospNavyPrimary,
        header = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(HospGoldAccent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Logo",
                    tint = HospNavyDark
                )
            }
        }
    ) {
        primaryNavItems.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationRailItem(
                modifier = Modifier.testTag("rail_item_${item.screen.name.lowercase()}"),
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    if (item.screen == AppScreen.NOTIFIKASI && pendingNotificationCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = HospCrimson) {
                                    Text("$pendingNotificationCount")
                                }
                            }
                        ) {
                            Icon(item.icon, contentDescription = item.label)
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.label)
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = HospNavyDark,
                    selectedTextColor = HospGoldBright,
                    indicatorColor = HospGoldAccent,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
                )
            )
        }
    }
}
