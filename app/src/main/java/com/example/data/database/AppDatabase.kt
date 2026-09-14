package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.AppDao
import com.example.data.model.Dosen
import com.example.data.model.Nilai
import com.example.data.model.Notifikasi
import com.example.data.model.Pelajaran
import com.example.data.model.Siswa
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Dosen::class,
        Siswa::class,
        Pelajaran::class,
        Nilai::class,
        Notifikasi::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "grand_hospitality.db"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.appDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: AppDao) {
            // 1. Dosen Instruktur
            val dosenList = listOf(
                Dosen(
                    nip = "GHI-DSN-01",
                    nama = "Chef Ronald Prasetyo",
                    gelar = "M.Par., Certified Executive Chef",
                    spesialisasiJurusan = "Culinary Arts",
                    email = "ronald.prasetyo@grandhospitality.ac.id",
                    noHp = "0812-3456-7801",
                    statusAktif = true
                ),
                Dosen(
                    nip = "GHI-DSN-02",
                    nama = "Ayu Paramitha",
                    gelar = "S.Tr.Par., CHT (Hospitality Trainer)",
                    spesialisasiJurusan = "Food & Beverage Service",
                    email = "ayu.paramitha@grandhospitality.ac.id",
                    noHp = "0812-3456-7802",
                    statusAktif = true
                ),
                Dosen(
                    nip = "GHI-DSN-03",
                    nama = "Dimas Pratama",
                    gelar = "S.E., M.M., CHA (Hotel Administrator)",
                    spesialisasiJurusan = "Front Office",
                    email = "dimas.pratama@grandhospitality.ac.id",
                    noHp = "0812-3456-7803",
                    statusAktif = true
                ),
                Dosen(
                    nip = "GHI-DSN-04",
                    nama = "Siti Rahmawati",
                    gelar = "S.Par., Executive Housekeeper Certified",
                    spesialisasiJurusan = "Housekeeping",
                    email = "siti.rahmawati@grandhospitality.ac.id",
                    noHp = "0812-3456-7804",
                    statusAktif = true
                ),
                Dosen(
                    nip = "GHI-DSN-05",
                    nama = "Kevin Santoso",
                    gelar = "Q-Grader, Master Barista",
                    spesialisasiJurusan = "Barista & Mixology",
                    email = "kevin.santoso@grandhospitality.ac.id",
                    noHp = "0812-3456-7805",
                    statusAktif = true
                ),
                Dosen(
                    nip = "GHI-DSN-06",
                    nama = "Nadia Wulandari",
                    gelar = "S.Sos., CMP (Certified Meeting Professional)",
                    spesialisasiJurusan = "Hotel Event & Banquet Management",
                    email = "nadia.wulandari@grandhospitality.ac.id",
                    noHp = "0812-3456-7806",
                    statusAktif = true
                )
            )
            dao.insertAllDosen(dosenList)

            // 2. Siswa / Peserta Didik
            val siswaList = listOf(
                Siswa(
                    nim = "GHI-2024-001",
                    nama = "Aditya Pratama",
                    jurusan = "Culinary Arts",
                    semester = 2,
                    angkatan = "Batch XXIV (2024)",
                    statusAkademik = "Aktif",
                    noHp = "0813-8899-1001",
                    email = "aditya.p@student.grandhospitality.ac.id",
                    statusSpp = "Lunas"
                ),
                Siswa(
                    nim = "GHI-2024-002",
                    nama = "Bella Clarissa",
                    jurusan = "Food & Beverage Service",
                    semester = 2,
                    angkatan = "Batch XXIV (2024)",
                    statusAkademik = "Aktif",
                    noHp = "0813-8899-1002",
                    email = "bella.c@student.grandhospitality.ac.id",
                    statusSpp = "Lunas"
                ),
                Siswa(
                    nim = "GHI-2024-003",
                    nama = "Christian Wijaya",
                    jurusan = "Front Office",
                    semester = 2,
                    angkatan = "Batch XXIV (2024)",
                    statusAkademik = "Aktif",
                    noHp = "0813-8899-1003",
                    email = "christian.w@student.grandhospitality.ac.id",
                    statusSpp = "Menunggu"
                ),
                Siswa(
                    nim = "GHI-2024-004",
                    nama = "Dewi Anggraini",
                    jurusan = "Housekeeping",
                    semester = 2,
                    angkatan = "Batch XXIV (2024)",
                    statusAkademik = "Aktif",
                    noHp = "0813-8899-1004",
                    email = "dewi.a@student.grandhospitality.ac.id",
                    statusSpp = "Lunas"
                ),
                Siswa(
                    nim = "GHI-2024-005",
                    nama = "Fajar Hidayat",
                    jurusan = "Barista & Mixology",
                    semester = 2,
                    angkatan = "Batch XXIV (2024)",
                    statusAkademik = "Aktif",
                    noHp = "0813-8899-1005",
                    email = "fajar.h@student.grandhospitality.ac.id",
                    statusSpp = "Menunggak"
                ),
                Siswa(
                    nim = "GHI-2023-018",
                    nama = "Gisella Anastasia",
                    jurusan = "Hotel Event & Banquet Management",
                    semester = 4,
                    angkatan = "Batch XXIII (2023)",
                    statusAkademik = "OJT / Magang Hotel Bintang 5",
                    noHp = "0813-8899-1006",
                    email = "gisella.a@student.grandhospitality.ac.id",
                    statusSpp = "Lunas"
                )
            )
            dao.insertAllSiswa(siswaList)

            // 3. Pelajaran
            val pelajaranList = listOf(
                Pelajaran(
                    kode = "CUL-101",
                    nama = "Dasar Pengolahan Makanan & Pisau (Knife Skills)",
                    sks = 3,
                    semester = 1,
                    jurusan = "Culinary Arts",
                    dosenPengampu = "Chef Ronald Prasetyo",
                    kategori = "Praktik Perhotelan"
                ),
                Pelajaran(
                    kode = "CUL-201",
                    nama = "Hot Kitchen & Continental Cuisine",
                    sks = 4,
                    semester = 2,
                    jurusan = "Culinary Arts",
                    dosenPengampu = "Chef Ronald Prasetyo",
                    kategori = "Praktik Perhotelan"
                ),
                Pelajaran(
                    kode = "CUL-202",
                    nama = "Pastry, Bakery & Dessert Arts",
                    sks = 3,
                    semester = 2,
                    jurusan = "Culinary Arts",
                    dosenPengampu = "Chef Ronald Prasetyo",
                    kategori = "Praktik Perhotelan"
                ),
                Pelajaran(
                    kode = "FBS-201",
                    nama = "Table Service, Banquet & Fine Dining",
                    sks = 4,
                    semester = 2,
                    jurusan = "Food & Beverage Service",
                    dosenPengampu = "Ayu Paramitha",
                    kategori = "Praktik Perhotelan"
                ),
                Pelajaran(
                    kode = "FBS-202",
                    nama = "Beverage Knowledge & Wine Etiquette",
                    sks = 3,
                    semester = 2,
                    jurusan = "Food & Beverage Service",
                    dosenPengampu = "Ayu Paramitha",
                    kategori = "Praktik Perhotelan"
                ),
                Pelajaran(
                    kode = "FO-201",
                    nama = "Front Desk, PMS Opera & Guest Relations",
                    sks = 4,
                    semester = 2,
                    jurusan = "Front Office",
                    dosenPengampu = "Dimas Pratama",
                    kategori = "Praktik Perhotelan"
                ),
                Pelajaran(
                    kode = "HK-201",
                    nama = "Room Attendant & Public Area Management",
                    sks = 4,
                    semester = 2,
                    jurusan = "Housekeeping",
                    dosenPengampu = "Siti Rahmawati",
                    kategori = "Praktik Perhotelan"
                ),
                Pelajaran(
                    kode = "BAR-201",
                    nama = "Espresso Extraction, Latte Art & Mocktail",
                    sks = 4,
                    semester = 2,
                    jurusan = "Barista & Mixology",
                    dosenPengampu = "Kevin Santoso",
                    kategori = "Praktik Perhotelan"
                ),
                Pelajaran(
                    kode = "EVM-201",
                    nama = "MICE Planning & Banquet Operation",
                    sks = 4,
                    semester = 2,
                    jurusan = "Hotel Event & Banquet Management",
                    dosenPengampu = "Nadia Wulandari",
                    kategori = "Teori & Manajemen"
                ),
                Pelajaran(
                    kode = "HSP-101",
                    nama = "Hospitality English & Professional Grooming",
                    sks = 2,
                    semester = 2,
                    jurusan = "Semua Jurusan",
                    dosenPengampu = "Ayu Paramitha",
                    kategori = "Teori & Manajemen"
                )
            )
            dao.insertAllPelajaran(pelajaranList)

            // 4. Nilai Pre-seeded
            val nilaiList = listOf(
                // Siswa 1 (Aditya Pratama - Culinary Arts)
                Nilai(
                    siswaId = 1,
                    pelajaranId = 2, // CUL-201
                    semester = 2,
                    nilaiKehadiran = 95.0,
                    nilaiTugas = 88.0,
                    nilaiPraktik = 92.0,
                    nilaiUts = 90.0,
                    nilaiUas = 91.0,
                    nilaiAkhir = 91.05,
                    nilaiHuruf = "A",
                    bobot = 4.0,
                    statusKelulusan = "Kompeten"
                ),
                Nilai(
                    siswaId = 1,
                    pelajaranId = 3, // CUL-202
                    semester = 2,
                    nilaiKehadiran = 90.0,
                    nilaiTugas = 85.0,
                    nilaiPraktik = 88.0,
                    nilaiUts = 84.0,
                    nilaiUas = 86.0,
                    nilaiAkhir = 86.7,
                    nilaiHuruf = "A",
                    bobot = 4.0,
                    statusKelulusan = "Kompeten"
                ),
                Nilai(
                    siswaId = 1,
                    pelajaranId = 10, // HSP-101
                    semester = 2,
                    nilaiKehadiran = 85.0,
                    nilaiTugas = 82.0,
                    nilaiPraktik = 80.0,
                    nilaiUts = 80.0,
                    nilaiUas = 82.0,
                    nilaiAkhir = 81.2,
                    nilaiHuruf = "A-",
                    bobot = 3.7,
                    statusKelulusan = "Kompeten"
                ),
                // Siswa 2 (Bella Clarissa - F&B Service)
                Nilai(
                    siswaId = 2,
                    pelajaranId = 4, // FBS-201
                    semester = 2,
                    nilaiKehadiran = 98.0,
                    nilaiTugas = 90.0,
                    nilaiPraktik = 94.0,
                    nilaiUts = 92.0,
                    nilaiUas = 95.0,
                    nilaiAkhir = 93.45,
                    nilaiHuruf = "A",
                    bobot = 4.0,
                    statusKelulusan = "Kompeten"
                ),
                Nilai(
                    siswaId = 2,
                    pelajaranId = 5, // FBS-202
                    semester = 2,
                    nilaiKehadiran = 92.0,
                    nilaiTugas = 86.0,
                    nilaiPraktik = 89.0,
                    nilaiUts = 85.0,
                    nilaiUas = 88.0,
                    nilaiAkhir = 87.95,
                    nilaiHuruf = "A",
                    bobot = 4.0,
                    statusKelulusan = "Kompeten"
                ),
                Nilai(
                    siswaId = 2,
                    pelajaranId = 10, // HSP-101
                    semester = 2,
                    nilaiKehadiran = 95.0,
                    nilaiTugas = 90.0,
                    nilaiPraktik = 92.0,
                    nilaiUts = 90.0,
                    nilaiUas = 92.0,
                    nilaiAkhir = 91.6,
                    nilaiHuruf = "A",
                    bobot = 4.0,
                    statusKelulusan = "Kompeten"
                ),
                // Siswa 3 (Christian Wijaya - Front Office)
                Nilai(
                    siswaId = 3,
                    pelajaranId = 6, // FO-201
                    semester = 2,
                    nilaiKehadiran = 90.0,
                    nilaiTugas = 82.0,
                    nilaiPraktik = 85.0,
                    nilaiUts = 80.0,
                    nilaiUas = 82.0,
                    nilaiAkhir = 83.7,
                    nilaiHuruf = "A-",
                    bobot = 3.7,
                    statusKelulusan = "Kompeten"
                ),
                Nilai(
                    siswaId = 3,
                    pelajaranId = 10, // HSP-101
                    semester = 2,
                    nilaiKehadiran = 92.0,
                    nilaiTugas = 85.0,
                    nilaiPraktik = 84.0,
                    nilaiUts = 82.0,
                    nilaiUas = 85.0,
                    nilaiAkhir = 84.85,
                    nilaiHuruf = "A-",
                    bobot = 3.7,
                    statusKelulusan = "Kompeten"
                ),
                // Siswa 4 (Dewi Anggraini - Housekeeping)
                Nilai(
                    siswaId = 4,
                    pelajaranId = 7, // HK-201
                    semester = 2,
                    nilaiKehadiran = 96.0,
                    nilaiTugas = 88.0,
                    nilaiPraktik = 93.0,
                    nilaiUts = 88.0,
                    nilaiUas = 90.0,
                    nilaiAkhir = 91.1,
                    nilaiHuruf = "A",
                    bobot = 4.0,
                    statusKelulusan = "Kompeten"
                ),
                Nilai(
                    siswaId = 4,
                    pelajaranId = 10,
                    semester = 2,
                    nilaiKehadiran = 88.0,
                    nilaiTugas = 82.0,
                    nilaiPraktik = 85.0,
                    nilaiUts = 80.0,
                    nilaiUas = 84.0,
                    nilaiAkhir = 83.8,
                    nilaiHuruf = "A-",
                    bobot = 3.7,
                    statusKelulusan = "Kompeten"
                ),
                // Siswa 5 (Fajar Hidayat - Barista)
                Nilai(
                    siswaId = 5,
                    pelajaranId = 8, // BAR-201
                    semester = 2,
                    nilaiKehadiran = 90.0,
                    nilaiTugas = 84.0,
                    nilaiPraktik = 91.0,
                    nilaiUts = 85.0,
                    nilaiUas = 89.0,
                    nilaiAkhir = 88.3,
                    nilaiHuruf = "A",
                    bobot = 4.0,
                    statusKelulusan = "Kompeten"
                ),
                Nilai(
                    siswaId = 5,
                    pelajaranId = 10,
                    semester = 2,
                    nilaiKehadiran = 80.0,
                    nilaiTugas = 75.0,
                    nilaiPraktik = 78.0,
                    nilaiUts = 70.0,
                    nilaiUas = 74.0,
                    nilaiAkhir = 75.8,
                    nilaiHuruf = "B+",
                    bobot = 3.3,
                    statusKelulusan = "Kompeten"
                )
            )
            dao.insertAllNilai(nilaiList)

            // 5. Notifikasi & Pengingat
            val notifikasiList = listOf(
                Notifikasi(
                    judul = "Ujian Praktik Table Manner & Fine Dining",
                    kategori = "Jadwal Ujian",
                    tanggalJatuhTempo = "15 Oktober 2026",
                    deskripsi = "Pelaksanaan Uji Praktik Table Manner di Restoran Praktik Kampus. Mahasiswa wajib berpakaian Grooming Lengkap (Black Tie & Vest).",
                    targetJurusan = "Food & Beverage Service",
                    status = "Pending"
                ),
                Notifikasi(
                    judul = "Batas Akhir Pembayaran SPP Tahap 2",
                    kategori = "Pembayaran Sekolah",
                    tanggalJatuhTempo = "20 Oktober 2026",
                    deskripsi = "Pemberitahuan kepada seluruh siswa yang belum melunasi SPP Semester Ganjil 2026/2027 agar menyelesaikan administrasi sebelum pekan UTS.",
                    targetJurusan = "Semua Jurusan",
                    status = "Pending"
                ),
                Notifikasi(
                    judul = "Ujian Praktik Hot Kitchen Cooking & Plating",
                    kategori = "Jadwal Ujian",
                    tanggalJatuhTempo = "24 Oktober 2026",
                    deskripsi = "Ujian praktikum Continental 3-Course Menu di Kitchen Lab Lt. 2. Harap membawa perlengkapan pisau pribadi & celemek standar HACCP.",
                    targetJurusan = "Culinary Arts",
                    status = "Pending"
                ),
                Notifikasi(
                    judul = "Pelunasan Biaya Sertifikasi BNSP Perhotelan",
                    kategori = "Pembayaran Sekolah",
                    tanggalJatuhTempo = "05 November 2026",
                    deskripsi = "Biaya pendaftaran uji kompetensi profesi perhotelan nasional (LSP/BNSP) untuk sertifikat kompetensi kerja resmi.",
                    targetJurusan = "Semua Jurusan",
                    status = "Pending"
                ),
                Notifikasi(
                    judul = "Sidang Laporan On-the-Job Training (OJT)",
                    kategori = "Jadwal Ujian",
                    tanggalJatuhTempo = "12 November 2026",
                    deskripsi = "Presentasi laporan magang 6 bulan di Hotel Bintang 4 & 5 di hadapan Dewan Penguji dan General Manager Penguji Tamu.",
                    targetJurusan = "Hotel Event & Banquet Management",
                    status = "Pending"
                )
            )
            dao.insertAllNotifikasi(notifikasiList)
        }
    }
}
