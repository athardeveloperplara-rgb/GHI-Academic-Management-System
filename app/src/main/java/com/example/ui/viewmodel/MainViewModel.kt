package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.Dosen
import com.example.data.model.Nilai
import com.example.data.model.NilaiDetail
import com.example.data.model.Notifikasi
import com.example.data.model.Pelajaran
import com.example.data.model.Siswa
import com.example.data.repository.HospitalityRepository
import com.example.data.util.GradeCalculator
import com.example.data.util.NotificationHelper
import com.example.data.util.PrintHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

enum class AppScreen(val label: String) {
    DASHBOARD("Dashboard"),
    DOSEN("Data Dosen"),
    SISWA("Data Siswa"),
    PELAJARAN("Pelajaran"),
    NILAI("Kelola Nilai"),
    LAPORAN("Cetak Laporan"),
    NOTIFIKASI("Pengingat & Notif"),
    ANALITIK("Dasbor Analitik")
}

data class AnalyticsData(
    val totalSiswa: Int = 0,
    val totalDosen: Int = 0,
    val totalPelajaran: Int = 0,
    val rataRataIpk: Double = 0.0,
    val persentaseKelulusan: Double = 0.0,
    val rataRataPraktik: Double = 0.0,
    val rataRataTeori: Double = 0.0,
    val sppLunasCount: Int = 0,
    val sppMenungguCount: Int = 0,
    val sppMenunggakCount: Int = 0,
    val ipkPerJurusan: Map<String, Double> = emptyMap(),
    val topStudents: List<Pair<Siswa, Double>> = emptyList()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HospitalityRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = HospitalityRepository(db.appDao())
    }

    // --- AUTHENTICATION STATE ---
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUsername = MutableStateFlow("admin")
    val currentUsername: StateFlow<String> = _currentUsername.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    fun login(usernameInput: String, passwordInput: String): Boolean {
        val trimmedUser = usernameInput.trim()
        val trimmedPass = passwordInput.trim()
        if ((trimmedUser.equals("admin", ignoreCase = true) || trimmedUser.equals("staf", ignoreCase = true)) &&
            (trimmedPass == "grand123" || trimmedPass == "admin123" || trimmedPass == "grandhospitality123")
        ) {
            _currentUsername.value = if (trimmedUser.equals("admin", true)) "Administrator Akademik" else "Staf Administrasi"
            _isLoggedIn.value = true
            _loginError.value = null
            return true
        } else {
            _loginError.value = "Username atau password salah! (Gunakan: admin / grand123)"
            return false
        }
    }

    fun quickLogin() {
        _currentUsername.value = "Staf Administrasi Akademik"
        _isLoggedIn.value = true
        _loginError.value = null
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentScreen.value = AppScreen.DASHBOARD
    }

    // --- NAVIGATION STATE ---
    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // --- DATA FLOWS ---
    val dosenList: StateFlow<List<Dosen>> = repository.allDosen
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val siswaList: StateFlow<List<Siswa>> = repository.allSiswa
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pelajaranList: StateFlow<List<Pelajaran>> = repository.allPelajaran
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val nilaiList: StateFlow<List<Nilai>> = repository.allNilai
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val nilaiDetailList: StateFlow<List<NilaiDetail>> = repository.allNilaiDetails
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifikasiList: StateFlow<List<Notifikasi>> = repository.allNotifikasi
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- REAL-TIME ANALYTICS FLOW ---
    val analyticsData: StateFlow<AnalyticsData> = combine(
        siswaList,
        dosenList,
        pelajaranList,
        nilaiList
    ) { siswa, dosen, pelajaran, nilai ->
        calculateAnalytics(siswa, dosen, pelajaran, nilai)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalyticsData())

    private fun calculateAnalytics(
        siswa: List<Siswa>,
        dosen: List<Dosen>,
        pelajaran: List<Pelajaran>,
        nilai: List<Nilai>
    ): AnalyticsData {
        if (siswa.isEmpty()) {
            return AnalyticsData(totalDosen = dosen.size, totalPelajaran = pelajaran.size)
        }

        val totalSiswa = siswa.size
        val totalDosen = dosen.size
        val totalPelajaran = pelajaran.size

        // SPP counts
        val lunas = siswa.count { it.statusSpp.equals("Lunas", ignoreCase = true) }
        val menunggu = siswa.count { it.statusSpp.equals("Menunggu", ignoreCase = true) }
        val menunggak = siswa.count { it.statusSpp.equals("Menunggak", ignoreCase = true) }

        // Average scores
        val rataPraktik = if (nilai.isNotEmpty()) {
            String.format(Locale.US, "%.1f", nilai.map { it.nilaiPraktik }.average()).toDouble()
        } else 0.0

        val rataTeori = if (nilai.isNotEmpty()) {
            String.format(Locale.US, "%.1f", nilai.map { (it.nilaiTugas + it.nilaiUts + it.nilaiUas) / 3.0 }.average()).toDouble()
        } else 0.0

        val kompetenCount = nilai.count { it.statusKelulusan.equals("Kompeten", ignoreCase = true) }
        val persentaseKelulusan = if (nilai.isNotEmpty()) {
            String.format(Locale.US, "%.1f", (kompetenCount.toDouble() / nilai.size.toDouble()) * 100.0).toDouble()
        } else 100.0

        // IPK per Siswa
        val pelajaranMap = pelajaran.associateBy { it.id }
        val studentIpkList = mutableListOf<Pair<Siswa, Double>>()

        siswa.forEach { s ->
            val sNilai = nilai.filter { it.siswaId == s.id }
            if (sNilai.isNotEmpty()) {
                var totalBobotSks = 0.0
                var totalSks = 0
                sNilai.forEach { n ->
                    val sks = pelajaranMap[n.pelajaranId]?.sks ?: 3
                    totalBobotSks += (n.bobot * sks)
                    totalSks += sks
                }
                val ipk = if (totalSks > 0) totalBobotSks / totalSks else 0.0
                val roundedIpk = String.format(Locale.US, "%.2f", ipk).toDouble()
                studentIpkList.add(s to roundedIpk)
            } else {
                studentIpkList.add(s to 0.0)
            }
        }

        val rataIpk = if (studentIpkList.any { it.second > 0.0 }) {
            val nonZero = studentIpkList.filter { it.second > 0.0 }
            String.format(Locale.US, "%.2f", nonZero.map { it.second }.average()).toDouble()
        } else 0.0

        // IPK per Jurusan
        val ipkPerJurusan = studentIpkList
            .groupBy { it.first.jurusan }
            .mapValues { entry ->
                val list = entry.value.filter { it.second > 0.0 }
                if (list.isNotEmpty()) {
                    String.format(Locale.US, "%.2f", list.map { it.second }.average()).toDouble()
                } else 0.0
            }

        // Top 3 Students
        val topStudents = studentIpkList.sortedByDescending { it.second }.take(3)

        return AnalyticsData(
            totalSiswa = totalSiswa,
            totalDosen = totalDosen,
            totalPelajaran = totalPelajaran,
            rataRataIpk = rataIpk,
            persentaseKelulusan = persentaseKelulusan,
            rataRataPraktik = rataPraktik,
            rataRataTeori = rataTeori,
            sppLunasCount = lunas,
            sppMenungguCount = menunggu,
            sppMenunggakCount = menunggak,
            ipkPerJurusan = ipkPerJurusan,
            topStudents = topStudents
        )
    }

    // --- DOSEN CRUD ---
    fun addDosen(dosen: Dosen) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDosen(dosen)
        }
    }

    fun updateDosen(dosen: Dosen) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDosen(dosen)
        }
    }

    fun deleteDosen(dosen: Dosen) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDosen(dosen)
        }
    }

    // --- SISWA CRUD ---
    fun addSiswa(siswa: Siswa) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSiswa(siswa)
        }
    }

    fun updateSiswa(siswa: Siswa) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSiswa(siswa)
        }
    }

    fun deleteSiswa(siswa: Siswa) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSiswa(siswa)
        }
    }

    // --- PELAJARAN CRUD ---
    fun addPelajaran(pelajaran: Pelajaran) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPelajaran(pelajaran)
        }
    }

    fun updatePelajaran(pelajaran: Pelajaran) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePelajaran(pelajaran)
        }
    }

    fun deletePelajaran(pelajaran: Pelajaran) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePelajaran(pelajaran)
        }
    }

    // --- NILAI CRUD & INPUT/EDIT ---
    fun saveOrUpdateNilai(
        id: Long = 0,
        siswaId: Long,
        pelajaranId: Long,
        semester: Int,
        kehadiran: Double,
        tugas: Double,
        praktik: Double,
        uts: Double,
        uas: Double
    ) {
        val calc = GradeCalculator.calculate(kehadiran, tugas, praktik, uts, uas)
        val nilai = Nilai(
            id = id,
            siswaId = siswaId,
            pelajaranId = pelajaranId,
            semester = semester,
            nilaiKehadiran = kehadiran,
            nilaiTugas = tugas,
            nilaiPraktik = praktik,
            nilaiUts = uts,
            nilaiUas = uas,
            nilaiAkhir = calc.nilaiAkhir,
            nilaiHuruf = calc.nilaiHuruf,
            bobot = calc.bobot,
            statusKelulusan = calc.statusKelulusan
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (id == 0L) {
                repository.insertNilai(nilai)
            } else {
                repository.updateNilai(nilai)
            }
        }
    }

    fun deleteNilai(nilai: Nilai) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNilai(nilai)
        }
    }

    // --- NOTIFIKASI CRUD & SYSTEM TRIGGER ---
    fun addNotifikasi(notifikasi: Notifikasi) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNotifikasi(notifikasi)
        }
    }

    fun toggleNotifikasiStatus(notifikasi: Notifikasi) {
        val newStatus = if (notifikasi.status == "Pending") "Selesai" else "Pending"
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNotifikasi(notifikasi.copy(status = newStatus))
        }
    }

    fun deleteNotifikasi(notifikasi: Notifikasi) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNotifikasi(notifikasi)
        }
    }

    fun sendSystemNotification(context: Context, notifikasi: Notifikasi): Boolean {
        return NotificationHelper.showNotification(
            context = context,
            notificationId = notifikasi.id.toInt(),
            title = notifikasi.judul,
            message = "${notifikasi.deskripsi} (Batas: ${notifikasi.tanggalJatuhTempo})",
            category = notifikasi.kategori
        )
    }

    // --- LAPORAN & CETAK REKAPITULASI ---
    private val _selectedStudentIdForReport = MutableStateFlow<Long?>(1L)
    val selectedStudentIdForReport: StateFlow<Long?> = _selectedStudentIdForReport.asStateFlow()

    private val _selectedSemesterForReport = MutableStateFlow(2)
    val selectedSemesterForReport: StateFlow<Int> = _selectedSemesterForReport.asStateFlow()

    fun selectStudentForReport(studentId: Long, semester: Int = 2) {
        _selectedStudentIdForReport.value = studentId
        _selectedSemesterForReport.value = semester
    }

    fun setSemesterForReport(semester: Int) {
        _selectedSemesterForReport.value = semester
    }

    fun printCurrentReport(context: Context) {
        val studentId = _selectedStudentIdForReport.value ?: return
        val semester = _selectedSemesterForReport.value
        val siswa = siswaList.value.find { it.id == studentId } ?: return
        val details = nilaiDetailList.value.filter {
            it.nilai.siswaId == studentId && it.nilai.semester == semester
        }

        val totalSks = details.sumOf { it.pelajaranSks }
        val totalBobot = details.sumOf { it.nilai.bobot * it.pelajaranSks }
        val ips = if (totalSks > 0) String.format(Locale.US, "%.2f", totalBobot / totalSks).toDouble() else 0.0
        val predikat = GradeCalculator.getPredikat(ips)

        val html = PrintHelper.generateHtmlTranscript(
            siswa = siswa,
            semester = semester,
            nilaiList = details,
            totalSks = totalSks,
            ips = ips,
            ipk = ips,
            predikat = predikat
        )
        PrintHelper.printDocument(context, html, "KHS_${siswa.nim}_Sem$semester")
    }

    fun shareCurrentReport(context: Context) {
        val studentId = _selectedStudentIdForReport.value ?: return
        val semester = _selectedSemesterForReport.value
        val siswa = siswaList.value.find { it.id == studentId } ?: return
        val details = nilaiDetailList.value.filter {
            it.nilai.siswaId == studentId && it.nilai.semester == semester
        }

        val totalSks = details.sumOf { it.pelajaranSks }
        val totalBobot = details.sumOf { it.nilai.bobot * it.pelajaranSks }
        val ips = if (totalSks > 0) String.format(Locale.US, "%.2f", totalBobot / totalSks).toDouble() else 0.0
        val predikat = GradeCalculator.getPredikat(ips)

        val text = PrintHelper.generateShareableText(
            siswa = siswa,
            semester = semester,
            nilaiList = details,
            totalSks = totalSks,
            ips = ips,
            ipk = ips,
            predikat = predikat
        )
        PrintHelper.shareTranscript(context, text)
    }
}
