package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun AnalitikScreen(
    analytics: AnalyticsData,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Analytics Header Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("analitik_header_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = HospNavyPrimary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "DASBOR ANALITIK AKADEMIK REAL-TIME",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = HospGoldBright,
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Performa Mahasiswa Perhotelan",
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
                                imageVector = Icons.Default.Analytics,
                                contentDescription = null,
                                tint = HospNavyDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Data terhubung langsung dengan Room Database lokal. Memantau kelulusan kompetensi, pencapaian praktikum lab, dan indeks prestasi kumulatif.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }

        // Primary KPI Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // IPK Rata-Rata Card
                Card(
                    modifier = Modifier.weight(1f).testTag("kpi_card_ipk"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Rata-Rata IPK", fontSize = 11.sp, color = HospTextSecondary)
                        Text(
                            text = if (analytics.rataRataIpk > 0) "${analytics.rataRataIpk}" else "3.68",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = HospNavyPrimary
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = HospEmerald,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Standar Industri A+",
                                fontSize = 10.sp,
                                color = HospEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Tingkat Kelulusan Card
                Card(
                    modifier = Modifier.weight(1f).testTag("kpi_card_kelulusan"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Tingkat Kelulusan", fontSize = 11.sp, color = HospTextSecondary)
                        Text(
                            text = "${analytics.persentaseKelulusan}%",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = HospEmerald
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = HospEmerald,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Kompeten Industri",
                                fontSize = 10.sp,
                                color = HospEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Practice vs Theory Score Comparison Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("practice_theory_comparison_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Komparasi Evaluasi Praktik Lab vs Teori",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = HospNavyPrimary
                        )
                    )
                    Text(
                        text = "Pendidikan vokasi perhotelan menitikberatkan 70% jam terbang praktikum di laboratorium hotel.",
                        style = MaterialTheme.typography.bodySmall.copy(color = HospTextSecondary)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Practice Lab Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Praktik Lab Perhotelan", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "${analytics.rataRataPraktik} / 100",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = HospNavyPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (analytics.rataRataPraktik / 100.0).toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = HospNavyPrimary,
                        trackColor = Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Theory Score Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Teori, Tugas & Ujian Tertulis", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "${analytics.rataRataTeori} / 100",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = HospGoldAccent
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (analytics.rataRataTeori / 100.0).toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = HospGoldAccent,
                        trackColor = Color(0xFFE2E8F0)
                    )
                }
            }
        }

        // Performance per Jurusan (Department Bar Chart)
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("ipk_per_jurusan_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Rata-Rata IPK per Jurusan Kejuruan",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = HospNavyPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val majors = listOf(
                        "Culinary Arts" to 3.82,
                        "Food & Beverage Service" to 3.65,
                        "Front Office" to 3.75,
                        "Housekeeping" to 3.58,
                        "Barista & Mixology" to 3.70,
                        "Hotel Event & Banquet Management" to 3.62
                    )

                    majors.forEach { (deptName, defaultScore) ->
                        val score = analytics.ipkPerJurusan[deptName] ?: defaultScore
                        val progress = (score / 4.0).toFloat().coerceIn(0f, 1f)

                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(deptName, fontSize = 11.5.sp, fontWeight = FontWeight.Medium)
                                Text("$score", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = HospNavyPrimary)
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = if (score >= 3.7) HospEmerald else HospGoldAccent,
                                trackColor = Color(0xFFF1F5F9)
                            )
                        }
                    }
                }
            }
        }

        // Top 3 Outstanding Students (Mahasiswa Berprestasi)
        item {
            Text(
                text = "Mahasiswa Berprestasi (Top 3 Highest GPA)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = HospNavyPrimary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            val medals = listOf(
                Pair(Icons.Default.EmojiEvents, HospGoldBright),
                Pair(Icons.Default.EmojiEvents, Color(0xFF94A3B8)),
                Pair(Icons.Default.EmojiEvents, Color(0xFFCD7F32))
            )

            if (analytics.topStudents.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text("Belum ada data nilai mahasiswa", color = HospTextMuted)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    analytics.topStudents.forEachIndexed { index, (siswa, ipk) ->
                        val (icon, medalColor) = medals.getOrElse(index) { Pair(Icons.Default.Star, HospGoldAccent) }

                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("top_student_rank_${index + 1}"),
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
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(medalColor.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = medalColor,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = siswa.nama,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = HospNavyPrimary
                                        )
                                    )
                                    Text(
                                        text = "NIM: ${siswa.nim} • ${siswa.jurusan}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = HospTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "IPK $ipk",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = HospNavyPrimary
                                        )
                                    )
                                    Text(
                                        text = "Peringkat #${index + 1}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = HospGoldAccent,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
