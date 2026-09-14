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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
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
import com.example.data.model.Dosen
import com.example.data.model.Pelajaran
import com.example.ui.theme.HospCrimson
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

@Composable
fun PelajaranScreen(
    pelajaranList: List<Pelajaran>,
    dosenList: List<Dosen>,
    onAddPelajaran: (Pelajaran) -> Unit,
    onUpdatePelajaran: (Pelajaran) -> Unit,
    onDeletePelajaran: (Pelajaran) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedJurusanFilter by remember { mutableStateOf("Semua") }

    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingPelajaran by remember { mutableStateOf<Pelajaran?>(null) }
    var pelajaranToDelete by remember { mutableStateOf<Pelajaran?>(null) }

    val filterOptions = listOf(
        "Semua",
        "Culinary Arts",
        "Food & Beverage Service",
        "Front Office",
        "Housekeeping",
        "Barista & Mixology",
        "Hotel Event & Banquet Management",
        "Semua Jurusan"
    )

    val filteredList = pelajaranList.filter { pel ->
        val matchesSearch = pel.nama.contains(searchQuery, ignoreCase = true) ||
                pel.kode.contains(searchQuery, ignoreCase = true) ||
                pel.dosenPengampu.contains(searchQuery, ignoreCase = true)
        val matchesJurusan = selectedJurusanFilter == "Semua" ||
                pel.jurusan.equals(selectedJurusanFilter, ignoreCase = true)
        matchesSearch && matchesJurusan
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingPelajaran = null
                    showAddEditDialog = true
                },
                modifier = Modifier.testTag("add_pelajaran_fab"),
                containerColor = HospGoldAccent,
                contentColor = HospNavyDark
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Mata Kuliah")
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

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_pelajaran_input"),
                placeholder = { Text("Cari Pelajaran berdasarkan Nama, Kode, atau Dosen...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = HospNavyPrimary)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterOptions) { filter ->
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

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Kurikulum & Mata Pelajaran (${filteredList.size})",
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
                            imageVector = Icons.Default.Book,
                            contentDescription = null,
                            tint = HospTextMuted,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tidak ada mata pelajaran yang ditemukan",
                            style = MaterialTheme.typography.bodyMedium.copy(color = HospTextMuted)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList, key = { it.id }) { pel ->
                        PelajaranCard(
                            pelajaran = pel,
                            onEdit = {
                                editingPelajaran = pel
                                showAddEditDialog = true
                            },
                            onDelete = {
                                pelajaranToDelete = pel
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

    // Add / Edit Dialog
    if (showAddEditDialog) {
        AddEditPelajaranDialog(
            pelajaran = editingPelajaran,
            dosenList = dosenList,
            onDismiss = { showAddEditDialog = false },
            onSave = { savedPelajaran ->
                if (editingPelajaran == null) {
                    onAddPelajaran(savedPelajaran)
                } else {
                    onUpdatePelajaran(savedPelajaran)
                }
                showAddEditDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    pelajaranToDelete?.let { pel ->
        AlertDialog(
            onDismissRequest = { pelajaranToDelete = null },
            title = { Text("Konfirmasi Hapus Mata Kuliah") },
            text = {
                Text("Hapus mata kuliah '${pel.nama}' (${pel.kode})?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeletePelajaran(pel)
                        pelajaranToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HospCrimson)
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { pelajaranToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun PelajaranCard(
    pelajaran: Pelajaran,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPraktik = pelajaran.kategori.contains("Praktik", ignoreCase = true)
    val badgeBg = if (isPraktik) HospEmeraldLight else Color(0xFFE0E7FF)
    val badgeColor = if (isPraktik) HospEmerald else Color(0xFF4338CA)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("pelajaran_card_${pelajaran.id}"),
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
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(HospNavyLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${pelajaran.sks}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = HospGoldBright
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = pelajaran.kode,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = HospNavyPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "• Semester ${pelajaran.semester}",
                                style = MaterialTheme.typography.labelSmall.copy(color = HospTextMuted)
                            )
                        }
                        Text(
                            text = pelajaran.nama,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = HospNavyPrimary
                            )
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(30.dp)
                            .testTag("edit_pelajaran_${pelajaran.id}")
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
                            .testTag("delete_pelajaran_${pelajaran.id}")
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

            Spacer(modifier = Modifier.height(10.dp))

            // Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = pelajaran.kategori,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = badgeColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(HospGoldLight)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = pelajaran.jurusan,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = HospNavyDark,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = HospNavyLight,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Dosen: ${pelajaran.dosenPengampu}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = HospTextSecondary,
                        fontSize = 11.5.sp
                    )
                )
            }
        }
    }
}

@Composable
fun AddEditPelajaranDialog(
    pelajaran: Pelajaran?,
    dosenList: List<Dosen>,
    onDismiss: () -> Unit,
    onSave: (Pelajaran) -> Unit
) {
    val isEdit = pelajaran != null
    var kode by remember { mutableStateOf(pelajaran?.kode ?: "GHI-${(101..499).random()}") }
    var nama by remember { mutableStateOf(pelajaran?.nama ?: "") }
    var sks by remember { mutableStateOf(pelajaran?.sks ?: 3) }
    var semester by remember { mutableStateOf(pelajaran?.semester ?: 2) }
    var jurusan by remember { mutableStateOf(pelajaran?.jurusan ?: "Culinary Arts") }
    var dosenPengampu by remember {
        mutableStateOf(pelajaran?.dosenPengampu ?: (dosenList.firstOrNull()?.nama ?: "Chef Ronald Prasetyo"))
    }
    var kategori by remember { mutableStateOf(pelajaran?.kategori ?: "Praktik Perhotelan") }

    val departments = listOf(
        "Culinary Arts",
        "Food & Beverage Service",
        "Front Office",
        "Housekeeping",
        "Barista & Mixology",
        "Hotel Event & Banquet Management",
        "Semua Jurusan"
    )

    val kategoriOptions = listOf("Praktik Perhotelan", "Teori & Manajemen")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEdit) "Edit Pelajaran" else "Tambah Pelajaran Baru")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = kode,
                        onValueChange = { kode = it },
                        label = { Text("Kode Mapel") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = sks.toString(),
                        onValueChange = { sks = it.toIntOrNull() ?: 3 },
                        label = { Text("Beban SKS") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Pelajaran / Mata Kuliah") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    text = "Kategori Kurikulum:",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    kategoriOptions.forEach { kat ->
                        FilterChip(
                            selected = kategori == kat,
                            onClick = { kategori = kat },
                            label = { Text(kat, fontSize = 11.sp) }
                        )
                    }
                }

                Text(
                    text = "Jurusan Target:",
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

                OutlinedTextField(
                    value = dosenPengampu,
                    onValueChange = { dosenPengampu = it },
                    label = { Text("Dosen / Instruktur Pengampu") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nama.isNotBlank()) {
                        val newPel = Pelajaran(
                            id = pelajaran?.id ?: 0,
                            kode = kode,
                            nama = nama,
                            sks = sks,
                            semester = semester,
                            jurusan = jurusan,
                            dosenPengampu = dosenPengampu,
                            kategori = kategori
                        )
                        onSave(newPel)
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
