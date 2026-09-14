package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Dosen
import com.example.data.model.Nilai
import com.example.data.model.Notifikasi
import com.example.data.model.Pelajaran
import com.example.data.model.Siswa
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // --- DOSEN ---
    @Query("SELECT * FROM dosen ORDER BY nama ASC")
    fun getAllDosen(): Flow<List<Dosen>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDosen(dosen: Dosen): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDosen(list: List<Dosen>)

    @Update
    suspend fun updateDosen(dosen: Dosen)

    @Delete
    suspend fun deleteDosen(dosen: Dosen)

    // --- SISWA ---
    @Query("SELECT * FROM siswa ORDER BY nama ASC")
    fun getAllSiswa(): Flow<List<Siswa>>

    @Query("SELECT * FROM siswa WHERE id = :id LIMIT 1")
    suspend fun getSiswaById(id: Long): Siswa?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSiswa(siswa: Siswa): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSiswa(list: List<Siswa>)

    @Update
    suspend fun updateSiswa(siswa: Siswa)

    @Delete
    suspend fun deleteSiswa(siswa: Siswa)

    // --- PELAJARAN ---
    @Query("SELECT * FROM pelajaran ORDER BY kode ASC")
    fun getAllPelajaran(): Flow<List<Pelajaran>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPelajaran(pelajaran: Pelajaran): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPelajaran(list: List<Pelajaran>)

    @Update
    suspend fun updatePelajaran(pelajaran: Pelajaran)

    @Delete
    suspend fun deletePelajaran(pelajaran: Pelajaran)

    // --- NILAI ---
    @Query("SELECT * FROM nilai ORDER BY id DESC")
    fun getAllNilai(): Flow<List<Nilai>>

    @Query("SELECT * FROM nilai WHERE siswaId = :siswaId")
    fun getNilaiBySiswa(siswaId: Long): Flow<List<Nilai>>

    @Query("SELECT * FROM nilai WHERE siswaId = :siswaId AND semester = :semester")
    fun getNilaiBySiswaAndSemester(siswaId: Long, semester: Int): Flow<List<Nilai>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNilai(nilai: Nilai): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNilai(list: List<Nilai>)

    @Update
    suspend fun updateNilai(nilai: Nilai)

    @Delete
    suspend fun deleteNilai(nilai: Nilai)

    // --- NOTIFIKASI ---
    @Query("SELECT * FROM notifikasi ORDER BY tanggalDibuat DESC")
    fun getAllNotifikasi(): Flow<List<Notifikasi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifikasi(notifikasi: Notifikasi): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNotifikasi(list: List<Notifikasi>)

    @Update
    suspend fun updateNotifikasi(notifikasi: Notifikasi)

    @Delete
    suspend fun deleteNotifikasi(notifikasi: Notifikasi)
}
