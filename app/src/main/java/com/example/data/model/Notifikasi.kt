package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifikasi")
data class Notifikasi(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val judul: String,
    val kategori: String, // Jadwal Ujian, Pembayaran SPP, Administrasi
    val tanggalJatuhTempo: String,
    val deskripsi: String,
    val targetJurusan: String = "Semua Jurusan",
    val status: String = "Pending", // Pending, Selesai
    val tanggalDibuat: Long = System.currentTimeMillis()
)
