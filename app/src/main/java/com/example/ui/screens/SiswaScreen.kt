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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Siswa
import com.example.ui.theme.HospAmber
import com.example.ui.theme.HospAmberLight
import com.example.ui.theme.HospCrimson
import com.example.ui.theme.HospCrimsonLight
import com.example.ui.theme.HospEmerald
import com.example.ui.theme.HospEmeraldLight
import com.example.ui.theme.HospGoldAccent
import com.example.ui.theme.HospGoldLight
import com.example.ui.theme.HospNavyDark
import com.example.ui.theme.HospNavyLight
import com.example.ui.theme.HospNavyPrimary
import com.example.ui.theme.HospTextMuted
import com.example.ui.theme.HospTextPrimary
import com.example.ui.theme.HospTextSecondary

@Composable
fun SiswaScreen(
    siswaList: List<Siswa>,
    onAddSiswa: (Siswa) -> Unit,
    onUpdateSiswa: (Siswa) -> Unit,
    onDeleteSiswa: (Siswa) -> Unit,
    onViewKhs: (Siswa) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedJurusanFilter by remember { mutableStateOf("Semua") }
    var selectedSemesterFilter by remember { mutableStateOf("Semua") }

    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingSiswa by remember { mutableStateOf<Siswa?>(null) }
    var siswaToDelete by remember { mutableStateOf<Siswa?>(null) }

    val jurusanOptions = listOf(
        "Semua",
        "Culinary Arts",
        "Food & Beverage Service",
        "Front Office",
        "Housekeeping",
        "Barista & Mixology",
        "Hotel Event & Banquet Management"
    )

    val semesterOptions = listOf("Semua", "Semester 1", "Semester 2", "Semester 3", "Semester 4")

    val filteredList = siswaList.filter { siswa ->
        val matchesSearch = siswa.nama.contains(searchQuery, ignoreCase = true) ||
                siswa.nim.contains(searchQuery, ignoreCase = true) ||
                siswa.jurusan.contains(searchQuery, ignoreCase = true)
        val matchesJurusan = selectedJurusanFilter == "Semua" ||
                siswa.jurusan.equals(selectedJurusanFilter, ignoreCase = true)
        val matchesSemester = selectedSemesterFilter == "Semua" ||
                "Semester ${siswa.semester}" == selectedSemesterFilter
        matchesSearch && matchesJurusan && matchesSemester
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingSiswa = null
                    showAddEditDialog = true
                },
                modifier = Modifier.testTag("add_siswa_fab"),
                containerColor = HospGoldAccent,
                contentColor = HospNavyDark
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Siswa")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_siswa_input"),
                placeholder = { Text("Cari Siswa berdasarkan Nama atau NIM...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = HospNavyPrimary)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Jurusan Filters
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(jurusanOptions) { filter ->
                    val isSelected = selectedJurusanFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedJurusanFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HospNavyPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Semester Filters
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(semesterOptions) { sem ->
                    val isSelected = selectedSemesterFilter == sem
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedSemesterFilter = sem },
                        label = {
                            Text(
                                text = sem,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HospGoldAccent,
                            selectedLabelColor = HospNavyDark
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Daftar Siswa & Mahasiswa Perhotelan (${filteredList.size})",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = HospNavyPrimary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = HospTextMuted,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tidak ada siswa yang sesuai dengan filter atau kata kunci",
                            style = MaterialTheme.typography.bodyMedium.copy(color = HospTextMuted)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList, key = { it.id }) { siswa ->
                        SiswaCard(
                            siswa = siswa,
                            onViewKhs = { onViewKhs(siswa) },
                            onEdit = {
                                editingSiswa = siswa
                                showAddEditDialog = true
                            },
                            onDelete = {
                                siswaToDelete = siswa
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }
    }

    // Add / Edit Siswa Dialog
    if (showAddEditDialog) {
        AddEditSiswaDialog(
            siswa = editingSiswa,
            onDismiss = { showAddEditDialog = false },
            onSave = { savedSiswa ->
                if (editingSiswa == null) {
                    onAddSiswa(savedSiswa)
                } else {
                    onUpdateSiswa(savedSiswa)
                }
                showAddEditDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    siswaToDelete?.let { siswa ->
        AlertDialog(
            onDismissRequest = { siswaToDelete = null },
            title = { Text("Konfirmasi Hapus Siswa") },
            text = {
                Text("Hapus data siswa '${siswa.nama}' (${siswa.nim}) beserta semua riwayat nilainya?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteSiswa(siswa)
                        siswaToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HospCrimson)
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { siswaToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun SiswaCard(
    siswa: Siswa,
    onViewKhs: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sppBg = when (siswa.statusSpp) {
        "Lunas" -> HospEmeraldLight
        "Menunggu" -> HospAmberLight
        else -> HospCrimsonLight
    }
    val sppColor = when (siswa.statusSpp) {
        "Lunas" -> HospEmerald
        "Menunggu" -> HospAmber
        else -> HospCrimson
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("siswa_card_${siswa.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(HospGoldLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = siswa.nama.take(2).uppercase(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = HospNavyPrimary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = siswa.nama,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = HospNavyPrimary
                            )
                        )
                        Text(
                            text = "NIM: ${siswa.nim} • Semester ${siswa.semester}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = HospTextSecondary,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(30.dp)
                            .testTag("edit_siswa_${siswa.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = HospNavyLight,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(30.dp)
                            .testTag("delete_siswa_${siswa.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus",
                            tint = HospCrimson,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Jurusan & Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(HospNavyLight.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = siswa.jurusan,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = HospNavyPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp
                        )
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Status Akademik
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = siswa.statusAkademik,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                color = HospTextSecondary
                            )
                        )
                    }

                    // Status SPP
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(sppBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "SPP: ${siswa.statusSpp}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = sppColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom action: View KHS / Rekap Nilai
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = siswa.angkatan,
                    style = MaterialTheme.typography.labelSmall.copy(color = HospTextMuted)
                )

                OutlinedButton(
                    onClick = onViewKhs,
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("btn_view_khs_${siswa.id}"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = HospNavyPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Print,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Cetak / Rekap KHS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AddEditSiswaDialog(
    siswa: Siswa?,
    onDismiss: () -> Unit,
    onSave: (Siswa) -> Unit
) {
    val isEdit = siswa != null
    var nim by remember { mutableStateOf(siswa?.nim ?: "GHI-2024-00${(10..99).random()}") }
    var nama by remember { mutableStateOf(siswa?.nama ?: "") }
    var jurusan by remember { mutableStateOf(siswa?.jurusan ?: "Culinary Arts") }
    var semester by remember { mutableStateOf(siswa?.semester ?: 2) }
    var angkatan by remember { mutableStateOf(siswa?.angkatan ?: "Batch XXIV (2024)") }
    var statusAkademik by remember { mutableStateOf(siswa?.statusAkademik ?: "Aktif") }
    var statusSpp by remember { mutableStateOf(siswa?.statusSpp ?: "Lunas") }
    var noHp by remember { mutableStateOf(siswa?.noHp ?: "0813-") }
    var email by remember { mutableStateOf(siswa?.email ?: "") }

    val departments = listOf(
        "Culinary Arts",
        "Food & Beverage Service",
        "Front Office",
        "Housekeeping",
        "Barista & Mixology",
        "Hotel Event & Banquet Management"
    )

    val sppStatusOptions = listOf("Lunas", "Menunggu", "Menunggak")
    val academicStatusOptions = listOf("Aktif", "OJT / Magang Hotel", "Lulus")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEdit) "Edit Data Siswa" else "Tambah Siswa Baru")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nim,
                    onValueChange = { nim = it },
                    label = { Text("Nomor Induk Siswa (NIM)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Lengkap Siswa") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    text = "Pilih Jurusan Perhotelan:",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(departments) { dept ->
                        FilterChip(
                            selected = jurusan == dept,
                            onClick = { jurusan = dept },
                            label = { Text(dept, fontSize = 11.sp) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = semester.toString(),
                        onValueChange = { semester = it.toIntOrNull() ?: 1 },
                        label = { Text("Semester") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = angkatan,
                        onValueChange = { angkatan = it },
                        label = { Text("Angkatan/Batch") },
                        modifier = Modifier.weight(2f),
                        singleLine = true
                    )
                }

                Text(
                    text = "Status Pembayaran SPP:",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    sppStatusOptions.forEach { st ->
                        FilterChip(
                            selected = statusSpp == st,
                            onClick = { statusSpp = st },
                            label = { Text(st, fontSize = 11.sp) }
                        )
                    }
                }

                Text(
                    text = "Status Akademik:",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    academicStatusOptions.forEach { act ->
                        FilterChip(
                            selected = statusAkademik == act,
                            onClick = { statusAkademik = act },
                            label = { Text(act, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = noHp,
                    onValueChange = { noHp = it },
                    label = { Text("No. HP Siswa/Wali") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nama.isNotBlank()) {
                        val newSiswa = Siswa(
                            id = siswa?.id ?: 0,
                            nim = nim,
                            nama = nama,
                            jurusan = jurusan,
                            semester = semester,
                            angkatan = angkatan,
                            statusAkademik = statusAkademik,
                            noHp = noHp,
                            email = if (email.isBlank()) "${nama.lowercase().replace(" ", ".")}@student.grandhospitality.ac.id" else email,
                            statusSpp = statusSpp
                        )
                        onSave(newSiswa)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = HospNavyPrimary)
            ) {
                Text(if (isEdit) "Perbarui" else "Simpan", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
