package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Notifikasi
import com.example.ui.theme.HospAmber
import com.example.ui.theme.HospAmberLight
import com.example.ui.theme.HospCardBorder
import com.example.ui.theme.HospCrimson
import com.example.ui.theme.HospCrimsonLight
import com.example.ui.theme.HospEmerald
import com.example.ui.theme.HospEmeraldLight
import com.example.ui.theme.HospGoldAccent
import com.example.ui.theme.HospGoldBright
import com.example.ui.theme.HospGoldLight
import com.example.ui.theme.HospNavyDark
import com.example.ui.theme.HospNavyLight
import com.example.ui.theme.HospNavyPrimary
import com.example.ui.theme.HospTextMuted
import com.example.ui.theme.HospTextPrimary
import com.example.ui.theme.HospTextSecondary
import com.example.ui.viewmodel.AnalyticsData
import com.example.ui.viewmodel.AppScreen

@Composable
fun DashboardScreen(
    staffName: String,
    analytics: AnalyticsData,
    upcomingReminders: List<Notifikasi>,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Welcome Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_welcome_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = HospNavyPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "LKP GRAND HOSPITALITY INSTITUTE",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = HospGoldBright,
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Selamat Datang, $staffName",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(HospGoldAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = HospNavyDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Semester Ganjil 2026/2027 • Sistem Terhubung Real-Time",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4ADE80))
                        )
                    }
                }
            }
        }

        // 4 Primary Metric Stat Cards
        item {
            Text(
                text = "Ringkasan Akademik Lembaga",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = HospNavyPrimary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Siswa Aktif",
                    value = "${analytics.totalSiswa}",
                    subtitle = "6 Jurusan",
                    icon = Icons.Default.People,
                    iconBg = HospGoldLight,
                    iconTint = HospNavyPrimary,
                    onClick = { onNavigate(AppScreen.SISWA) }
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Dosen / Chef",
                    value = "${analytics.totalDosen}",
                    subtitle = "Instruktur Ahli",
                    icon = Icons.Default.Person,
                    iconBg = HospEmeraldLight,
                    iconTint = HospEmerald,
                    onClick = { onNavigate(AppScreen.DOSEN) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Mata Pelajaran",
                    value = "${analytics.totalPelajaran}",
                    subtitle = "Praktik & Teori",
                    icon = Icons.Default.Book,
                    iconBg = Color(0xFFE0E7FF),
                    iconTint = Color(0xFF4338CA),
                    onClick = { onNavigate(AppScreen.PELAJARAN) }
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Rata-Rata IPK",
                    value = if (analytics.rataRataIpk > 0) "${analytics.rataRataIpk}" else "3.68",
                    subtitle = "Kelulusan ${analytics.persentaseKelulusan}%",
                    icon = Icons.Default.Grade,
                    iconBg = HospAmberLight,
                    iconTint = HospAmber,
                    onClick = { onNavigate(AppScreen.ANALITIK) }
                )
            }
        }

        // Quick Action Buttons
        item {
            Text(
                text = "Aksi Cepat Administrasi",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = HospNavyPrimary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionButton(
                    modifier = Modifier.weight(1f),
                    label = "Input Nilai",
                    icon = Icons.Default.Grade,
                    containerColor = HospNavyPrimary,
                    contentColor = Color.White,
                    onClick = { onNavigate(AppScreen.NILAI) }
                )
                QuickActionButton(
                    modifier = Modifier.weight(1f),
                    label = "Cetak KHS",
                    icon = Icons.Default.Print,
                    containerColor = HospGoldAccent,
                    contentColor = HospNavyDark,
                    onClick = { onNavigate(AppScreen.LAPORAN) }
                )
                QuickActionButton(
                    modifier = Modifier.weight(1f),
                    label = "Analitik",
                    icon = Icons.Default.Analytics,
                    containerColor = HospNavyLight,
                    contentColor = Color.White,
                    onClick = { onNavigate(AppScreen.ANALITIK) }
                )
            }
        }

        // SPP Payment Status Overview Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_spp_status_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                tint = HospNavyPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Status Pembayaran Uang Sekolah / SPP",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            text = "Detail Siswa",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = HospNavyLight,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.clickable { onNavigate(AppScreen.SISWA) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PaymentStatusBadge(
                            label = "Lunas",
                            count = analytics.sppLunasCount,
                            bgColor = HospEmeraldLight,
                            textColor = HospEmerald,
                            modifier = Modifier.weight(1f)
                        )
                        PaymentStatusBadge(
                            label = "Menunggu",
                            count = analytics.sppMenungguCount,
                            bgColor = HospAmberLight,
                            textColor = HospAmber,
                            modifier = Modifier.weight(1f)
                        )
                        PaymentStatusBadge(
                            label = "Menunggak",
                            count = analytics.sppMenunggakCount,
                            bgColor = HospCrimsonLight,
                            textColor = HospCrimson,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Upcoming Exam and Payment Reminders Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pengingat Ujian & SPP Terdekat",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = HospNavyPrimary
                    )
                )
                Text(
                    text = "Lihat Semua",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = HospNavyLight,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { onNavigate(AppScreen.NOTIFIKASI) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            val pendingReminders = upcomingReminders.filter { it.status == "Pending" }.take(3)
            if (pendingReminders.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Semua jadwal ujian dan pembayaran sekolah telah terselesaikan!",
                            style = MaterialTheme.typography.bodySmall.copy(color = HospTextMuted)
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pendingReminders.forEach { notif ->
                        ReminderItemCard(
                            notif = notif,
                            onClick = { onNavigate(AppScreen.NOTIFIKASI) }
                        )
                    }
                }
            }
        }

        // Hospitality Majors Overview
        item {
            Text(
                text = "Jurusan Kejuruan Perhotelan",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = HospNavyPrimary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            val majors = listOf(
                "Culinary Arts (Tata Boga & Pastry)",
                "Food & Beverage Service",
                "Front Office (Kantor Depan)",
                "Housekeeping (Tata Graha)",
                "Barista & Mixology Beverage",
                "Hotel Event & Banquet Management"
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    majors.forEachIndexed { idx, major ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigate(AppScreen.SISWA) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(HospGoldLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = HospNavyPrimary
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = major,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = HospTextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = HospTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag("stat_card_${title.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = HospTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = HospNavyPrimary
                )
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = HospTextMuted
                )
            )
        }
    }
}

@Composable
fun QuickActionButton(
    label: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(44.dp)
            .testTag("quick_action_${label.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun PaymentStatusBadge(
    label: String,
    count: Int,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun ReminderItemCard(
    notif: Notifikasi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isExam = notif.kategori == "Jadwal Ujian"
    val badgeBg = if (isExam) HospGoldLight else HospCrimsonLight
    val badgeTint = if (isExam) HospNavyPrimary else HospCrimson

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("dashboard_reminder_card_${notif.id}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(badgeBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isExam) Icons.Default.DateRange else Icons.Default.Payment,
                    contentDescription = null,
                    tint = badgeTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notif.kategori.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = badgeTint,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        text = " • ${notif.tanggalJatuhTempo}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = HospTextMuted,
                            fontSize = 10.sp
                        )
                    )
                }
                Text(
                    text = notif.judul,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = HospTextPrimary
                    )
                )
                Text(
                    text = notif.targetJurusan,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = HospTextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
