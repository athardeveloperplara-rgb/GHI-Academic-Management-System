package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dosen")
data class Dosen(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nip: String,
    val nama: String,
    val gelar: String,
    val spesialisasiJurusan: String,
    val email: String,
    val noHp: String,
    val statusAktif: Boolean = true
)
