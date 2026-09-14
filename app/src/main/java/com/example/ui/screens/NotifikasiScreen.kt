package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payment
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Notifikasi
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

@Composable
fun NotifikasiScreen(
    notifikasiList: List<Notifikasi>,
    onAddNotifikasi: (Notifikasi) -> Unit,
    onToggleStatus: (Notifikasi) -> Unit,
    onDeleteNotifikasi: (Notifikasi) -> Unit,
    onTriggerNotification: (Context, Notifikasi) -> Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategoryFilter by remember { mutableStateOf("Semua") }
    var showAddDialog by remember { mutableStateOf(false) }
    var notifToDelete by remember { mutableStateOf<Notifikasi?>(null) }

    // Permission launcher for Android 13+ Notification
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Izin notifikasi aktif! Anda dapat mengirim pengingat.", Toast.LENGTH_SHORT).show()
        }
    }

    val filterOptions = listOf("Semua", "Jadwal Ujian", "Pembayaran Sekolah", "Pending", "Selesai")

    val filteredList = notifikasiList.filter { notif ->
        when (selectedCategoryFilter) {
            "Semua" -> true
            "Jadwal Ujian" -> notif.kategori.equals("Jadwal Ujian", ignoreCase = true)
            "Pembayaran Sekolah" -> notif.kategori.equals("Pembayaran Sekolah", ignoreCase = true)
            "Pending" -> notif.status.equals("Pending", ignoreCase = true)
            "Selesai" -> notif.status.equals("Selesai", ignoreCase = true)
            else -> true
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.testTag("add_notifikasi_fab"),
                containerColor = HospGoldAccent,
                contentColor = HospNavyDark
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Pengingat")
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

            // Header Banner
            Card(
                modifier = Modifier.fillMaxWidth().testTag("notifikasi_header_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = HospNavyPrimary)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(HospGoldAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = HospNavyDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Pusat Pengingat Akademik",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Kelola jadwal ujian vokasi & batas pembayaran SPP perhotelan",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterOptions) { filter ->
                    val isSelected = selectedCategoryFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryFilter = filter },
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

            Text(
                text = "Daftar Pengingat Aktif (${filteredList.size})",
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
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = HospTextMuted,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tidak ada notifikasi pengingat pada kategori ini",
                            style = MaterialTheme.typography.bodyMedium.copy(color = HospTextMuted)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList, key = { it.id }) { notif ->
                        NotifikasiCard(
                            notif = notif,
                            onToggleStatus = { onToggleStatus(notif) },
                            onTrigger = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                                val sent = onTriggerNotification(context, notif)
                                if (sent) {
                                    Toast.makeText(context, "Notifikasi '${notif.judul}' berhasil dikirim ke perangkat!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onDelete = { notifToDelete = notif }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }
    }

    // Add Notification Dialog
    if (showAddDialog) {
        AddNotifikasiDialog(
            onDismiss = { showAddDialog = false },
            onSave = { newNotif ->
                onAddNotifikasi(newNotif)
                showAddDialog = false
                Toast.makeText(context, "Pengingat baru berhasil dibuat!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Delete Confirmation
    notifToDelete?.let { notif ->
        AlertDialog(
            onDismissRequest = { notifToDelete = null },
            title = { Text("Hapus Pengingat") },
            text = { Text("Hapus notifikasi '${notif.judul}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteNotifikasi(notif)
                        notifToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HospCrimson)
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { notifToDelete = null }) { Text("Batal") }
            }
        )
    }
}

@Composable
fun NotifikasiCard(
    notif: Notifikasi,
    onToggleStatus: () -> Unit,
    onTrigger: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isExam = notif.kategori == "Jadwal Ujian"
    val isDone = notif.status == "Selesai"

    val iconBg = if (isExam) HospGoldLight else HospCrimsonLight
    val iconTint = if (isExam) HospNavyPrimary else HospCrimson

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("notifikasi_card_${notif.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDone) Color(0xFFF8FAFC) else Color.White
        ),
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isExam) Icons.Default.DateRange else Icons.Default.Payment,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = notif.kategori.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = iconTint,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "• Batas: ${notif.tanggalJatuhTempo}",
                                style = MaterialTheme.typography.labelSmall.copy(color = HospTextMuted)
                            )
                        }
                        Text(
                            text = notif.judul,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDone) HospTextMuted else HospNavyPrimary
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(30.dp).testTag("delete_notif_${notif.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus",
                        tint = HospCrimson,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = notif.deskripsi,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = HospTextSecondary,
                    fontSize = 11.5.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Target Major badge & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Sasaran: ${notif.targetJurusan}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = HospTextSecondary,
                            fontSize = 10.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isDone) HospEmeraldLight else HospGoldLight)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = notif.status,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isDone) HospEmerald else HospNavyDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons: Send System Notification & Toggle Done
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onTrigger,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("btn_kirim_notif_${notif.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HospNavyPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Kirim Notif Sistem", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onToggleStatus,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("btn_toggle_notif_${notif.id}"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isDone) "Tandai Belum" else "Selesai", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun AddNotifikasiDialog(
    onDismiss: () -> Unit,
    onSave: (Notifikasi) -> Unit
) {
    var judul by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("Jadwal Ujian") }
    var tanggalJatuhTempo by remember { mutableStateOf("25 Oktober 2026") }
    var targetJurusan by remember { mutableStateOf("Semua Jurusan Perhotelan") }
    var deskripsi by remember { mutableStateOf("") }

    val kategoriOptions = listOf("Jadwal Ujian", "Pembayaran Sekolah")
    val jurusanOptions = listOf(
        "Semua Jurusan Perhotelan",
        "Culinary Arts",
        "Food & Beverage Service",
        "Front Office",
        "Housekeeping",
        "Barista & Mixology",
        "Hotel Event & Banquet Management"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Buat Pengingat Akademik Baru") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Kategori Pengingat:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    kategoriOptions.forEach { kat ->
                        FilterChip(
                            selected = kategori == kat,
                            onClick = { kategori = kat },
                            label = { Text(kat, fontSize = 11.5.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = judul,
                    onValueChange = { judul = it },
                    label = { Text("Judul Pengingat") },
                    placeholder = { Text("cth. Batas Akhir Pembayaran SPP Tahap II") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = tanggalJatuhTempo,
                    onValueChange = { tanggalJatuhTempo = it },
                    label = { Text("Batas Tanggal / Jatuh Tempo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Target Jurusan Mahasiswa:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(jurusanOptions) { dept ->
                        FilterChip(
                            selected = targetJurusan == dept,
                            onClick = { targetJurusan = dept },
                            label = { Text(dept, fontSize = 10.5.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text("Deskripsi Informasi") },
                    placeholder = { Text("Detail petunjuk ujian / nomor rekening pembayaran...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (judul.isNotBlank()) {
                        val notif = Notifikasi(
                            judul = judul,
                            kategori = kategori,
                            tanggalJatuhTempo = tanggalJatuhTempo,
                            targetJurusan = targetJurusan,
                            deskripsi = deskripsi,
                            status = "Pending"
                        )
                        onSave(notif)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = HospNavyPrimary)
            ) {
                Text("Simpan Pengingat", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}
