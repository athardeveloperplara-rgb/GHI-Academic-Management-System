package com.example.data.repository

import com.example.data.dao.AppDao
import com.example.data.model.Dosen
import com.example.data.model.Nilai
import com.example.data.model.NilaiDetail
import com.example.data.model.Notifikasi
import com.example.data.model.Pelajaran
import com.example.data.model.Siswa
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class HospitalityRepository(private val dao: AppDao) {

    // --- DOSEN ---
    val allDosen: Flow<List<Dosen>> = dao.getAllDosen()

    suspend fun insertDosen(dosen: Dosen): Long = dao.insertDosen(dosen)
    suspend fun updateDosen(dosen: Dosen) = dao.updateDosen(dosen)
    suspend fun deleteDosen(dosen: Dosen) = dao.deleteDosen(dosen)

    // --- SISWA ---
    val allSiswa: Flow<List<Siswa>> = dao.getAllSiswa()

    suspend fun getSiswaById(id: Long): Siswa? = dao.getSiswaById(id)
    suspend fun insertSiswa(siswa: Siswa): Long = dao.insertSiswa(siswa)
    suspend fun updateSiswa(siswa: Siswa) = dao.updateSiswa(siswa)
    suspend fun deleteSiswa(siswa: Siswa) = dao.deleteSiswa(siswa)

    // --- PELAJARAN ---
    val allPelajaran: Flow<List<Pelajaran>> = dao.getAllPelajaran()

    suspend fun insertPelajaran(pelajaran: Pelajaran): Long = dao.insertPelajaran(pelajaran)
    suspend fun updatePelajaran(pelajaran: Pelajaran) = dao.updatePelajaran(pelajaran)
    suspend fun deletePelajaran(pelajaran: Pelajaran) = dao.deletePelajaran(pelajaran)

    // --- NILAI ---
    val allNilai: Flow<List<Nilai>> = dao.getAllNilai()

    fun getNilaiBySiswa(siswaId: Long): Flow<List<Nilai>> = dao.getNilaiBySiswa(siswaId)
    fun getNilaiBySiswaAndSemester(siswaId: Long, semester: Int): Flow<List<Nilai>> =
        dao.getNilaiBySiswaAndSemester(siswaId, semester)

    suspend fun insertNilai(nilai: Nilai): Long = dao.insertNilai(nilai)
    suspend fun updateNilai(nilai: Nilai) = dao.updateNilai(nilai)
    suspend fun deleteNilai(nilai: Nilai) = dao.deleteNilai(nilai)

    // --- JOINED NILAI DETAILS ---
    val allNilaiDetails: Flow<List<NilaiDetail>> = combine(
        allNilai,
        allSiswa,
        allPelajaran
    ) { nilaiList, siswaList, pelajaranList ->
        val siswaMap = siswaList.associateBy { it.id }
        val pelajaranMap = pelajaranList.associateBy { it.id }

        nilaiList.mapNotNull { nilai ->
            val siswa = siswaMap[nilai.siswaId]
            val pelajaran = pelajaranMap[nilai.pelajaranId]
            if (siswa != null && pelajaran != null) {
                NilaiDetail(
                    nilai = nilai,
                    siswaNim = siswa.nim,
                    siswaNama = siswa.nama,
                    siswaJurusan = siswa.jurusan,
                    pelajaranKode = pelajaran.kode,
                    pelajaranNama = pelajaran.nama,
                    pelajaranSks = pelajaran.sks,
                    pelajaranKategori = pelajaran.kategori,
                    dosenNama = pelajaran.dosenPengampu
                )
            } else null
        }
    }

    // --- NOTIFIKASI ---
    val allNotifikasi: Flow<List<Notifikasi>> = dao.getAllNotifikasi()

    suspend fun insertNotifikasi(notifikasi: Notifikasi): Long = dao.insertNotifikasi(notifikasi)
    suspend fun updateNotifikasi(notifikasi: Notifikasi) = dao.updateNotifikasi(notifikasi)
    suspend fun deleteNotifikasi(notifikasi: Notifikasi) = dao.deleteNotifikasi(notifikasi)
}
