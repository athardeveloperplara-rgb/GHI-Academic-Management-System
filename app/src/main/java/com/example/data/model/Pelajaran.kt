package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pelajaran")
data class Pelajaran(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val kode: String,
    val nama: String,
    val sks: Int,
    val semester: Int,
    val jurusan: String,
    val dosenPengampu: String,
    val kategori: String = "Praktik Perhotelan" // Praktik Perhotelan, Teori & Manajemen
)
