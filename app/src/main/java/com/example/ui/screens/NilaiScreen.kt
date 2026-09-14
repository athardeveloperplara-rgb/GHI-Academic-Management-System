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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.data.model.Nilai
import com.example.data.model.NilaiDetail
import com.example.data.model.Pelajaran
import com.example.data.model.Siswa
import com.example.data.util.GradeCalculator
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
import java.util.Locale

@Composable
fun NilaiScreen(
    siswaList: List<Siswa>,
    pelajaranList: List<Pelajaran>,
    nilaiList: List<Nilai>,
    nilaiDetailList: List<NilaiDetail>,
    onSaveNilai: (
        id: Long,
        siswaId: Long,
        pelajaranId: Long,
        semester: Int,
        kehadiran: Double,
        tugas: Double,
        praktik: Double,
        uts: Double,
        uas: Double
    ) -> Unit,
    onDeleteNilai: (Nilai) -> Unit,
    onNavigateToLaporan: (studentId: Long, semester: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Input Nilai", "Daftar & Edit", "Rekapitulasi Siswa")

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = HospNavyPrimary,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = HospGoldAccent,
                    height = 3.dp
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    modifier = Modifier.testTag("tab_nilai_$index"),
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) HospGoldBright else Color.White.copy(alpha = 0.8f)
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> TabInputNilai(
                siswaList = siswaList,
                pelajaranList = pelajaranList,
                onSave = onSaveNilai
            )
            1 -> TabDaftarEditNilai(
                nilaiDetailList = nilaiDetailList,
                siswaList = siswaList,
                pelajaranList = pelajaranList,
                onSave = onSaveNilai,
                onDelete = onDeleteNilai
            )
            2 -> TabRekapitulasiNilai(
                siswaList = siswaList,
                nilaiDetailList = nilaiDetailList,
                onCetakKhs = onNavigateToLaporan
            )
        }
    }
}

// -------------------------------------------------------------
// TAB 1: INPUT NILAI BARU
// -------------------------------------------------------------
@Composable
fun TabInputNilai(
    siswaList: List<Siswa>,
    pelajaranList: List<Pelajaran>,
    onSave: (
        id: Long,
        siswaId: Long,
        pelajaranId: Long,
        semester: Int,
        kehadiran: Double,
        tugas: Double,
        praktik: Double,
        uts: Double,
        uas: Double
    ) -> Unit
) {
    var selectedSiswa by remember(siswaList) { mutableStateOf(siswaList.firstOrNull()) }
    var selectedPelajaran by remember(pelajaranList) { mutableStateOf(pelajaranList.firstOrNull()) }
    var semester by remember { mutableStateOf(2) }

    var kehadiranStr by remember { mutableStateOf("90") }
    var tugasStr by remember { mutableStateOf("85") }
    var praktikStr by remember { mutableStateOf("88") }
    var utsStr by remember { mutableStateOf("84") }
    var uasStr by remember { mutableStateOf("86") }

    var showSuccessBanner by remember { mutableStateOf(false) }

    val kehadiranVal = kehadiranStr.toDoubleOrNull() ?: 0.0
    val tugasVal = tugasStr.toDoubleOrNull() ?: 0.0
    val praktikVal = praktikStr.toDoubleOrNull() ?: 0.0
    val utsVal = utsStr.toDoubleOrNull() ?: 0.0
    val uasVal = uasStr.toDoubleOrNull() ?: 0.0

    val calcPreview = GradeCalculator.calculate(
        kehadiran = kehadiranVal,
        tugas = tugasVal,
        praktik = praktikVal,
        uts = utsVal,
        uas = uasVal
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Form Input Nilai Mahasiswa Perhotelan",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = HospNavyPrimary
                )
            )
            Text(
                text = "Bobot penilaian vokasi perhotelan: Presensi (10%), Tugas Teori (20%), Praktik Lab (40%), UTS (15%), UAS (15%)",
                style = MaterialTheme.typography.bodySmall.copy(color = HospTextSecondary)
            )
        }

        if (showSuccessBanner) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HospEmeraldLight)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HospEmerald)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Nilai berhasil disimpan ke basis data akademik!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = HospEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        item {
            // Student Dropdown Selector
            var siswaExpanded by remember { mutableStateOf(false) }
            Text("Pilih Siswa / Mahasiswa:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedSiswa?.let { "${it.nama} (${it.nim}) - ${it.jurusan}" } ?: "Pilih Siswa",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { siswaExpanded = true }
                        .testTag("dropdown_select_siswa"),
                    trailingIcon = {
                        IconButton(onClick = { siswaExpanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = siswaExpanded,
                    onDismissRequest = { siswaExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    siswaList.forEach { s ->
                        DropdownMenuItem(
                            text = { Text("${s.nama} (${s.nim}) - ${s.jurusan}") },
                            onClick = {
                                selectedSiswa = s
                                semester = s.semester
                                siswaExpanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            // Course Dropdown Selector
            var pelExpanded by remember { mutableStateOf(false) }
            Text("Pilih Mata Kuliah / Pelajaran:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedPelajaran?.let { "[${it.kode}] ${it.nama} (${it.sks} SKS)" } ?: "Pilih Pelajaran",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { pelExpanded = true }
                        .testTag("dropdown_select_pelajaran"),
                    trailingIcon = {
                        IconButton(onClick = { pelExpanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = pelExpanded,
                    onDismissRequest = { pelExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    pelajaranList.forEach { p ->
                        DropdownMenuItem(
                            text = { Text("[${p.kode}] ${p.nama} (${p.sks} SKS - ${p.kategori})") },
                            onClick = {
                                selectedPelajaran = p
                                pelExpanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            // Semester Selector
            Text("Semester:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..4).forEach { sem ->
                    FilterChip(
                        selected = semester == sem,
                        onClick = { semester = sem },
                        label = { Text("Semester $sem") }
                    )
                }
            }
        }

        item {
            // Score Component Inputs
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Komponen Nilai Evaluasi (Skala 0 - 100):",
                        fontWeight = FontWeight.Bold,
                        color = HospNavyPrimary
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = kehadiranStr,
                            onValueChange = { kehadiranStr = it },
                            label = { Text("Presensi (10%)") },
                            modifier = Modifier.weight(1f).testTag("input_nilai_kehadiran"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = tugasStr,
                            onValueChange = { tugasStr = it },
                            label = { Text("Tugas (20%)") },
                            modifier = Modifier.weight(1f).testTag("input_nilai_tugas"),
                            singleLine = true
                        )
                    }

                    // Highlight for practical lab score
                    OutlinedTextField(
                        value = praktikStr,
                        onValueChange = { praktikStr = it },
                        label = { Text("Praktik Lab Perhotelan (40%) *Utama") },
                        modifier = Modifier.fillMaxWidth().testTag("input_nilai_praktik"),
                        singleLine = true
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = utsStr,
                            onValueChange = { utsStr = it },
                            label = { Text("UTS (15%)") },
                            modifier = Modifier.weight(1f).testTag("input_nilai_uts"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = uasStr,
                            onValueChange = { uasStr = it },
                            label = { Text("UAS (15%)") },
                            modifier = Modifier.weight(1f).testTag("input_nilai_uas"),
                            singleLine = true
                        )
                    }
                }
            }
        }

        item {
            // Live Calculation Preview Card
            Card(
                modifier = Modifier.fillMaxWidth().testTag("live_calculation_preview_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = HospGoldLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Kalkulasi Otomatis Nilai Akhir:",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = HospNavyDark
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Nilai Angka", fontSize = 11.sp, color = HospTextSecondary)
                            Text(
                                text = "${calcPreview.nilaiAkhir}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = HospNavyPrimary
                                )
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Nilai Huruf", fontSize = 11.sp, color = HospTextSecondary)
                            Text(
                                text = calcPreview.nilaiHuruf,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = HospGoldAccent
                                )
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Bobot", fontSize = 11.sp, color = HospTextSecondary)
                            Text(
                                text = "${calcPreview.bobot}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = HospNavyDark
                                )
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Status", fontSize = 11.sp, color = HospTextSecondary)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (calcPreview.statusKelulusan == "Kompeten") HospEmeraldLight else Color(0xFFFFD1D1))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = calcPreview.statusKelulusan,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = if (calcPreview.statusKelulusan == "Kompeten") HospEmerald else HospCrimson,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    val sId = selectedSiswa?.id ?: return@Button
                    val pId = selectedPelajaran?.id ?: return@Button
                    onSave(
                        0L,
                        sId,
                        pId,
                        semester,
                        kehadiranVal,
                        tugasVal,
                        praktikVal,
                        utsVal,
                        uasVal
                    )
                    showSuccessBanner = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_simpan_nilai"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HospNavyPrimary)
            ) {
                Icon(Icons.Default.Grade, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Simpan Nilai Siswa", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// -------------------------------------------------------------
// TAB 2: DAFTAR & EDIT NILAI
// -------------------------------------------------------------
@Composable
fun TabDaftarEditNilai(
    nilaiDetailList: List<NilaiDetail>,
    siswaList: List<Siswa>,
    pelajaranList: List<Pelajaran>,
    onSave: (
        id: Long,
        siswaId: Long,
        pelajaranId: Long,
        semester: Int,
        kehadiran: Double,
        tugas: Double,
        praktik: Double,
        uts: Double,
        uas: Double
    ) -> Unit,
    onDelete: (Nilai) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var editingNilaiDetail by remember { mutableStateOf<NilaiDetail?>(null) }
    var deletingNilai by remember { mutableStateOf<Nilai?>(null) }

    val filteredList = nilaiDetailList.filter {
        it.siswaNama.contains(searchQuery, ignoreCase = true) ||
                it.siswaNim.contains(searchQuery, ignoreCase = true) ||
                it.pelajaranNama.contains(searchQuery, ignoreCase = true) ||
                it.pelajaranKode.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().testTag("search_nilai_input"),
            placeholder = { Text("Cari nilai berdasarkan nama siswa atau mata kuliah...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Seluruh Rekaman Nilai (${filteredList.size})",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = HospNavyPrimary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada data nilai yang terdaftar", color = HospTextMuted)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.nilai.id }) { item ->
                    NilaiItemRow(
                        detail = item,
                        onEdit = { editingNilaiDetail = item },
                        onDelete = { deletingNilai = item.nilai }
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }

    // Edit Nilai Dialog
    editingNilaiDetail?.let { detail ->
        EditNilaiDialog(
            detail = detail,
            onDismiss = { editingNilaiDetail = null },
            onSave = { id, sId, pId, sem, keh, tug, prak, uts, uas ->
                onSave(id, sId, pId, sem, keh, tug, prak, uts, uas)
                editingNilaiDetail = null
            }
        )
    }

    // Delete Nilai Dialog
    deletingNilai?.let { nilai ->
        AlertDialog(
            onDismissRequest = { deletingNilai = null },
            title = { Text("Hapus Nilai") },
            text = { Text("Apakah Anda yakin ingin menghapus rekaman nilai ini?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(nilai)
                        deletingNilai = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HospCrimson)
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingNilai = null }) { Text("Batal") }
            }
        )
    }
}

@Composable
fun NilaiItemRow(
    detail: NilaiDetail,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val n = detail.nilai
    Card(
        modifier = modifier.fillMaxWidth().testTag("nilai_row_${n.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = detail.siswaNama,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = HospNavyPrimary
                        )
                    )
                    Text(
                        text = "${detail.siswaNim} • ${detail.siswaJurusan}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = HospTextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(HospGoldLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = n.nilaiHuruf,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = HospNavyDark
                            )
                        )
                    }

                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp).testTag("btn_edit_nilai_${n.id}")) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = HospNavyLight, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp).testTag("btn_delete_nilai_${n.id}")) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = HospCrimson, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "[${detail.pelajaranKode}] ${detail.pelajaranNama}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = HospTextPrimary
                        )
                    )
                    Text(
                        text = "Praktik: ${n.nilaiPraktik} | Tugas: ${n.nilaiTugas} | Presensi: ${n.nilaiKehadiran}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = HospTextMuted,
                            fontSize = 10.5.sp
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Akhir: ${n.nilaiAkhir}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = HospNavyPrimary
                        )
                    )
                    Text(
                        text = n.statusKelulusan,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (n.statusKelulusan == "Kompeten") HospEmerald else HospCrimson,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun EditNilaiDialog(
    detail: NilaiDetail,
    onDismiss: () -> Unit,
    onSave: (
        id: Long,
        siswaId: Long,
        pelajaranId: Long,
        semester: Int,
        kehadiran: Double,
        tugas: Double,
        praktik: Double,
        uts: Double,
        uas: Double
    ) -> Unit
) {
    val n = detail.nilai
    var kehadiranStr by remember { mutableStateOf("${n.nilaiKehadiran}") }
    var tugasStr by remember { mutableStateOf("${n.nilaiTugas}") }
    var praktikStr by remember { mutableStateOf("${n.nilaiPraktik}") }
    var utsStr by remember { mutableStateOf("${n.nilaiUts}") }
    var uasStr by remember { mutableStateOf("${n.nilaiUas}") }

    val calcPreview = GradeCalculator.calculate(
        kehadiran = kehadiranStr.toDoubleOrNull() ?: 0.0,
        tugas = tugasStr.toDoubleOrNull() ?: 0.0,
        praktik = praktikStr.toDoubleOrNull() ?: 0.0,
        uts = utsStr.toDoubleOrNull() ?: 0.0,
        uas = uasStr.toDoubleOrNull() ?: 0.0
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Edit Nilai Siswa", fontWeight = FontWeight.Bold)
                Text(
                    text = "${detail.siswaNama} - ${detail.pelajaranNama}",
                    style = MaterialTheme.typography.bodySmall,
                    color = HospTextSecondary
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = kehadiranStr,
                        onValueChange = { kehadiranStr = it },
                        label = { Text("Presensi (10%)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = tugasStr,
                        onValueChange = { tugasStr = it },
                        label = { Text("Tugas (20%)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                OutlinedTextField(
                    value = praktikStr,
                    onValueChange = { praktikStr = it },
                    label = { Text("Praktik Lab (40%)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = utsStr,
                        onValueChange = { utsStr = it },
                        label = { Text("UTS (15%)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = uasStr,
                        onValueChange = { uasStr = it },
                        label = { Text("UAS (15%)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(HospGoldLight)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Hasil: ${calcPreview.nilaiAkhir} (${calcPreview.nilaiHuruf})", fontWeight = FontWeight.Bold)
                        Text("Status: ${calcPreview.statusKelulusan}", color = HospEmerald, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        n.id,
                        n.siswaId,
                        n.pelajaranId,
                        n.semester,
                        kehadiranStr.toDoubleOrNull() ?: 0.0,
                        tugasStr.toDoubleOrNull() ?: 0.0,
                        praktikStr.toDoubleOrNull() ?: 0.0,
                        utsStr.toDoubleOrNull() ?: 0.0,
                        uasStr.toDoubleOrNull() ?: 0.0
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = HospNavyPrimary)
            ) {
                Text("Perbarui Nilai", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

// -------------------------------------------------------------
// TAB 3: REKAPITULASI NILAI PER SISWA PER SEMESTER
// -------------------------------------------------------------
@Composable
fun TabRekapitulasiNilai(
    siswaList: List<Siswa>,
    nilaiDetailList: List<NilaiDetail>,
    onCetakKhs: (studentId: Long, semester: Int) -> Unit
) {
    var selectedSiswa by remember(siswaList) { mutableStateOf(siswaList.firstOrNull()) }
    var selectedSemester by remember { mutableStateOf(2) }

    val studentDetails = selectedSiswa?.let { s ->
        nilaiDetailList.filter { it.nilai.siswaId == s.id && it.nilai.semester == selectedSemester }
    } ?: emptyList()

    val totalSks = studentDetails.sumOf { it.pelajaranSks }
    val totalBobot = studentDetails.sumOf { it.nilai.bobot * it.pelajaranSks }
    val ips = if (totalSks > 0) String.format(Locale.US, "%.2f", totalBobot / totalSks).toDouble() else 0.0
    val predikat = GradeCalculator.getPredikat(ips)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Rekapitulasi Nilai Akademik per Siswa",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = HospNavyPrimary
                )
            )
        }

        item {
            // Select Siswa Dropdown
            var siswaExpanded by remember { mutableStateOf(false) }
            Text("Pilih Siswa:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedSiswa?.let { "${it.nama} (${it.nim})" } ?: "Pilih Siswa",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { siswaExpanded = true }
                        .testTag("rekap_select_siswa"),
                    trailingIcon = {
                        IconButton(onClick = { siswaExpanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = siswaExpanded,
                    onDismissRequest = { siswaExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    siswaList.forEach { s ->
                        DropdownMenuItem(
                            text = { Text("${s.nama} (${s.nim}) - ${s.jurusan}") },
                            onClick = {
                                selectedSiswa = s
                                selectedSemester = s.semester
                                siswaExpanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            Text("Pilih Semester:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..4).forEach { sem ->
                    FilterChip(
                        selected = selectedSemester == sem,
                        onClick = { selectedSemester = sem },
                        label = { Text("Semester $sem") }
                    )
                }
            }
        }

        // Summary Card
        selectedSiswa?.let { siswa ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("rekap_summary_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = HospNavyPrimary)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = siswa.nama,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "NIM: ${siswa.nim} • Jurusan: ${siswa.jurusan}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = HospGoldBright,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }

                            Button(
                                onClick = { onCetakKhs(siswa.id, selectedSemester) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HospGoldAccent,
                                    contentColor = HospNavyDark
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("btn_cetak_rekap")
                            ) {
                                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Cetak KHS", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total SKS", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                Text("$totalSks SKS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Column {
                                Text("Indeks Semester (IPS)", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                Text("$ips", color = HospGoldBright, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Predikat Akademik", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                Text(predikat, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Daftar Nilai Semester $selectedSemester (${studentDetails.size} Pelajaran)",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = HospNavyPrimary
                    )
                )
            }

            if (studentDetails.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada nilai yang diinputkan untuk semester $selectedSemester",
                            style = MaterialTheme.typography.bodyMedium.copy(color = HospTextMuted)
                        )
                    }
                }
            } else {
                items(studentDetails) { detail ->
                    val n = detail.nilai
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = detail.pelajaranNama,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = HospTextPrimary
                                    )
                                )
                                Text(
                                    text = "Kode: ${detail.pelajaranKode} • ${detail.pelajaranSks} SKS • ${detail.pelajaranKategori}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = HospTextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = "Praktik: ${n.nilaiPraktik} | Teori: ${n.nilaiTugas} | Kehadiran: ${n.nilaiKehadiran}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = HospTextMuted,
                                        fontSize = 10.5.sp
                                    )
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${n.nilaiAkhir} (${n.nilaiHuruf})",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = HospNavyPrimary
                                    )
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (n.statusKelulusan == "Kompeten") HospEmeraldLight else Color(0xFFFFD1D1))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = n.statusKelulusan,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (n.statusKelulusan == "Kompeten") HospEmerald else HospCrimson,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.5.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
