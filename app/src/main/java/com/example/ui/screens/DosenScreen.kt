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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
fun DosenScreen(
    dosenList: List<Dosen>,
    onAddDosen: (Dosen) -> Unit,
    onUpdateDosen: (Dosen) -> Unit,
    onDeleteDosen: (Dosen) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedJurusanFilter by remember { mutableStateOf("Semua") }

    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingDosen by remember { mutableStateOf<Dosen?>(null) }
    var dosenToDelete by remember { mutableStateOf<Dosen?>(null) }

    val filterOptions = listOf(
        "Semua",
        "Culinary Arts",
        "Food & Beverage Service",
        "Front Office",
        "Housekeeping",
        "Barista & Mixology",
        "Hotel Event & Banquet Management"
    )

    val filteredList = dosenList.filter { dosen ->
        val matchesSearch = dosen.nama.contains(searchQuery, ignoreCase = true) ||
                dosen.nip.contains(searchQuery, ignoreCase = true) ||
                dosen.spesialisasiJurusan.contains(searchQuery, ignoreCase = true)
        val matchesJurusan = selectedJurusanFilter == "Semua" ||
                dosen.spesialisasiJurusan.equals(selectedJurusanFilter, ignoreCase = true)
        matchesSearch && matchesJurusan
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingDosen = null
                    showAddEditDialog = true
                },
                modifier = Modifier.testTag("add_dosen_fab"),
                containerColor = HospGoldAccent,
                contentColor = HospNavyDark
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Dosen")
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
                    .testTag("search_dosen_input"),
                placeholder = { Text("Cari Dosen/Instruktur berdasarkan nama atau NIP...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = HospNavyPrimary)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Department Filter Chips
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
                                fontSize = 12.sp,
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Dosen & Instruktur (${filteredList.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = HospNavyPrimary
                    )
                )
            }

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
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = HospTextMuted,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tidak ada dosen yang cocok dengan pencarian",
                            style = MaterialTheme.typography.bodyMedium.copy(color = HospTextMuted)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList, key = { it.id }) { dosen ->
                        DosenCard(
                            dosen = dosen,
                            onEdit = {
                                editingDosen = dosen
                                showAddEditDialog = true
                            },
                            onDelete = {
                                dosenToDelete = dosen
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
        AddEditDosenDialog(
            dosen = editingDosen,
            onDismiss = { showAddEditDialog = false },
            onSave = { savedDosen ->
                if (editingDosen == null) {
                    onAddDosen(savedDosen)
                } else {
                    onUpdateDosen(savedDosen)
                }
                showAddEditDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    dosenToDelete?.let { dosen ->
        AlertDialog(
            onDismissRequest = { dosenToDelete = null },
            title = { Text("Konfirmasi Hapus Dosen") },
            text = {
                Text("Apakah Anda yakin ingin menghapus data instruktur '${dosen.nama}'? Tindakan ini tidak dapat dibatalkan.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteDosen(dosen)
                        dosenToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HospCrimson)
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { dosenToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun DosenCard(
    dosen: Dosen,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dosen_card_${dosen.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(HospNavyLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dosen.nama.take(2).uppercase(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = HospGoldBright
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = dosen.nama,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = HospNavyPrimary
                            )
                        )
                        Text(
                            text = dosen.gelar,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = HospTextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("edit_dosen_${dosen.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Dosen",
                            tint = HospNavyLight,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("delete_dosen_${dosen.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus Dosen",
                            tint = HospCrimson,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Department badge & NIP
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(HospGoldLight)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = dosen.spesialisasiJurusan,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = HospNavyDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp
                        )
                    )
                }

                Text(
                    text = "NIP: ${dosen.nip}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = HospTextMuted,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Contact Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        tint = HospNavyLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = dosen.noHp,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = HospTextSecondary,
                            fontSize = 11.5.sp
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = HospNavyLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = dosen.email,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = HospTextSecondary,
                            fontSize = 11.5.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AddEditDosenDialog(
    dosen: Dosen?,
    onDismiss: () -> Unit,
    onSave: (Dosen) -> Unit
) {
    val isEdit = dosen != null
    var nip by remember { mutableStateOf(dosen?.nip ?: "GHI-DSN-0${(10..99).random()}") }
    var nama by remember { mutableStateOf(dosen?.nama ?: "") }
    var gelar by remember { mutableStateOf(dosen?.gelar ?: "S.Tr.Par., CHT") }
    var spesialisasi by remember { mutableStateOf(dosen?.spesialisasiJurusan ?: "Culinary Arts") }
    var email by remember { mutableStateOf(dosen?.email ?: "") }
    var noHp by remember { mutableStateOf(dosen?.noHp ?: "0812-") }
    var statusAktif by remember { mutableStateOf(dosen?.statusAktif ?: true) }

    val departments = listOf(
        "Culinary Arts",
        "Food & Beverage Service",
        "Front Office",
        "Housekeeping",
        "Barista & Mixology",
        "Hotel Event & Banquet Management"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEdit) "Edit Data Dosen" else "Tambah Dosen / Instruktur")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nip,
                    onValueChange = { nip = it },
                    label = { Text("NIP Dosen") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Lengkap") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = gelar,
                    onValueChange = { gelar = it },
                    label = { Text("Gelar & Sertifikasi Keahlian") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    text = "Spesialisasi Jurusan Perhotelan:",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(departments) { dept ->
                        FilterChip(
                            selected = spesialisasi == dept,
                            onClick = { spesialisasi = dept },
                            label = { Text(dept, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = noHp,
                    onValueChange = { noHp = it },
                    label = { Text("No. Handphone / WhatsApp") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Institusi") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nama.isNotBlank()) {
                        val newDosen = Dosen(
                            id = dosen?.id ?: 0,
                            nip = nip,
                            nama = nama,
                            gelar = gelar,
                            spesialisasiJurusan = spesialisasi,
                            email = if (email.isBlank()) "${nama.lowercase().replace(" ", ".")}@grandhospitality.ac.id" else email,
                            noHp = noHp,
                            statusAktif = statusAktif
                        )
                        onSave(newDosen)
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
