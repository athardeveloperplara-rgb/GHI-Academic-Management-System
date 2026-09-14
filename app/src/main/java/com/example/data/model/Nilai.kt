package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "nilai",
    foreignKeys = [
        ForeignKey(
            entity = Siswa::class,
            parentColumns = ["id"],
            childColumns = ["siswaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Pelajaran::class,
            parentColumns = ["id"],
            childColumns = ["pelajaranId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["siswaId"]),
        Index(value = ["pelajaranId"]),
        Index(value = ["siswaId", "pelajaranId", "semester"], unique = true)
    ]
)
data class Nilai(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val siswaId: Long,
    val pelajaranId: Long,
    val semester: Int,
    val nilaiKehadiran: Double, // Bobot 10%
    val nilaiTugas: Double,     // Bobot 20%
    val nilaiPraktik: Double,   // Bobot 40% (Laboratorium / Hospitality Kitchen / Room / Bar)
    val nilaiUts: Double,       // Bobot 15%
    val nilaiUas: Double,       // Bobot 15%
    val nilaiAkhir: Double,
    val nilaiHuruf: String,
    val bobot: Double,
    val statusKelulusan: String // Kompeten / Belum Kompeten
)
