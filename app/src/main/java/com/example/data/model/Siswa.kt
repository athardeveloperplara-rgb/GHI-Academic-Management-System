package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siswa")
data class Siswa(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nim: String,
    val nama: String,
    val jurusan: String,
    val semester: Int,
    val angkatan: String,
    val statusAkademik: String = "Aktif", // Aktif, Magang/OJT Hotel, Lulus
    val noHp: String,
    val email: String,
    val statusSpp: String = "Lunas" // Lunas, Menunggu, Menunggak
)
