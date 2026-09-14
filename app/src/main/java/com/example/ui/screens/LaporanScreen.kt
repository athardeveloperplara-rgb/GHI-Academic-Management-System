package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NilaiDetail
import com.example.data.model.Siswa
import com.example.data.util.GradeCalculator
import com.example.ui.theme.HospCardBorder
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LaporanScreen(
    siswaList: List<Siswa>,
    nilaiDetailList: List<NilaiDetail>,
    selectedStudentId: Long?,
    selectedSemester: Int,
    onSelectStudent: (Long, Int) -> Unit,
    onSetSemester: (Int) -> Unit,
    onPrintDocument: (Context) -> Unit,
    onShareDocument: (Context) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var studentDropdownExpanded by remember { mutableStateOf(false) }

    val currentSiswa = siswaList.find { it.id == selectedStudentId } ?: siswaList.firstOrNull()
    val studentGrades = currentSiswa?.let { s ->
        nilaiDetailList.filter { it.nilai.siswaId == s.id && it.nilai.semester == selectedSemester }
    } ?: emptyList()

    val totalSks = studentGrades.sumOf { it.pelajaranSks }
    val totalBobot = studentGrades.sumOf { it.nilai.bobot * it.pelajaranSks }
    val ips = if (totalSks > 0) String.format(Locale.US, "%.2f", totalBobot / totalSks).toDouble() else 0.0
    val predikat = GradeCalculator.getPredikat(ips)

    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    val currentDateStr = dateFormat.format(Date())

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Penerbitan & Cetak Laporan Akademik (KHS)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = HospNavyPrimary
                )
            )
            Text(
                text = "Pilih siswa dan semester untuk mencetak Kartu Hasil Studi resmi berstandar industri perhotelan.",
                style = MaterialTheme.typography.bodySmall.copy(color = HospTextSecondary)
            )
        }

        // Student and Semester Selectors
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Pilih Siswa:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = currentSiswa?.let { "${it.nama} (${it.nim}) - ${it.jurusan}" } ?: "Pilih Siswa",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { studentDropdownExpanded = true }
                                .testTag("laporan_select_siswa"),
                            trailingIcon = {
                                IconButton(onClick = { studentDropdownExpanded = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = studentDropdownExpanded,
                            onDismissRequest = { studentDropdownExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            siswaList.forEach { s ->
                                DropdownMenuItem(
                                    text = { Text("${s.nama} (${s.nim}) - ${s.jurusan}") },
                                    onClick = {
                                        onSelectStudent(s.id, s.semester)
                                        studentDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Text("Pilih Semester:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..4).forEach { sem ->
                            FilterChip(
                                selected = selectedSemester == sem,
                                onClick = { onSetSemester(sem) },
                                label = { Text("Semester $sem") }
                            )
                        }
                    }
                }
            }
        }

        // Primary Action Bar: Print & Share
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onPrintDocument(context) },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp)
                        .testTag("btn_cetak_laporan_pdf"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HospNavyPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Print, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cetak Dokumen (PDF)", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { onShareDocument(context) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_share_laporan_wa"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = HospNavyPrimary
                    )
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Bagikan", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Document Preview
        item {
            Text(
                text = "Pratinjau Dokumen Resmi (KHS)",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = HospNavyPrimary
                )
            )
        }

        item {
            // Document Canvas (Paper-like view)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("document_preview_sheet"),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Official Letterhead (KOP SURAT)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(HospGoldAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = HospNavyDark,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "LKP GRAND HOSPITALITY INSTITUTE",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = HospNavyPrimary,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "LEMBAGA KURSUS DAN PELATIHAN VOKASI PERHOTELAN TERAKREDITASI",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = HospTextSecondary,
                                    fontSize = 8.5.sp
                                )
                            )
                            Text(
                                text = "Izin Kemendikbudristek No: 421.9/108/DISDIK/2022 • SK Menkumham RI",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = HospTextMuted,
                                    fontSize = 8.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(HospNavyPrimary)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "KARTU HASIL STUDI (KHS) & TRANSKRIP AKADEMIK",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = HospNavyPrimary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Tahun Akademik 2026/2027 • Semester $selectedSemester",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = HospTextSecondary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Student Meta Data Table
                    currentSiswa?.let { s ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text("Nama Siswa", modifier = Modifier.weight(1.2f), fontSize = 11.sp, color = HospTextMuted)
                                Text(": ${s.nama}", modifier = Modifier.weight(2f), fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text("NIM", modifier = Modifier.weight(1.2f), fontSize = 11.sp, color = HospTextMuted)
                                Text(": ${s.nim}", modifier = Modifier.weight(2f), fontSize = 11.sp)
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text("Jurusan", modifier = Modifier.weight(1.2f), fontSize = 11.sp, color = HospTextMuted)
                                Text(": ${s.jurusan}", modifier = Modifier.weight(2f), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text("Angkatan", modifier = Modifier.weight(1.2f), fontSize = 11.sp, color = HospTextMuted)
                                Text(": ${s.angkatan}", modifier = Modifier.weight(2f), fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Grade Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(HospNavyPrimary)
                            .padding(vertical = 6.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("No", modifier = Modifier.width(24.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("Mata Pelajaran", modifier = Modifier.weight(1f), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("SKS", modifier = Modifier.width(32.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text("Nilai", modifier = Modifier.width(38.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text("Huruf", modifier = Modifier.width(36.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text("Mutu", modifier = Modifier.width(36.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                    }

                    // Grade Table Rows
                    if (studentGrades.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Belum ada data nilai untuk semester ini", fontSize = 11.sp, color = HospTextMuted)
                        }
                    } else {
                        studentGrades.forEachIndexed { idx, item ->
                            val n = item.nilai
                            val mutu = n.bobot * item.pelajaranSks
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${idx + 1}", modifier = Modifier.width(24.dp), fontSize = 10.5.sp, color = HospTextSecondary)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.pelajaranNama, fontSize = 10.5.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                                    Text(item.pelajaranKode, fontSize = 9.sp, color = HospTextMuted)
                                }
                                Text("${item.pelajaranSks}", modifier = Modifier.width(32.dp), fontSize = 10.5.sp, textAlign = TextAlign.Center)
                                Text("${n.nilaiAkhir}", modifier = Modifier.width(38.dp), fontSize = 10.5.sp, textAlign = TextAlign.Center)
                                Text(n.nilaiHuruf, modifier = Modifier.width(36.dp), fontSize = 10.5.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                Text("$mutu", modifier = Modifier.width(36.dp), fontSize = 10.5.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                            }
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Transcript Footer Totals
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(HospGoldLight)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Beban SKS: $totalSks SKS", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Total Bobot Mutu: $totalBobot", fontSize = 11.sp, color = HospTextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Indeks Prestasi Semester (IPS):", fontSize = 10.5.sp, color = HospTextSecondary)
                            Text("$ips", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = HospNavyPrimary)
                            Text("Predikat: $predikat", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = HospEmerald)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Signature block
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Mengetahui,", fontSize = 9.5.sp, color = HospTextSecondary)
                            Text("Instruktur Kepala Program,", fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(36.dp))
                            Text("( Chef Ronald Prasetyo, CHT )", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("NIP: GHI-DSN-0101", fontSize = 8.5.sp, color = HospTextMuted)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Jakarta, $currentDateStr", fontSize = 9.5.sp, color = HospTextSecondary)
                            Text("Direktur Akademik LKP GHI,", fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(36.dp))
                            Text("( Dr. Amanda Putri, M.Par )", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("NIP: GHI-DIR-0001", fontSize = 8.5.sp, color = HospTextMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
