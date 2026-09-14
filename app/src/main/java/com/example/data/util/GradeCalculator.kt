package com.example.data.util

import java.util.Locale

data class CalculationResult(
    val nilaiAkhir: Double,
    val nilaiHuruf: String,
    val bobot: Double,
    val statusKelulusan: String
)

object GradeCalculator {

    fun calculate(
        kehadiran: Double,
        tugas: Double,
        praktik: Double,
        uts: Double,
        uas: Double
    ): CalculationResult {
        // Bobot: Kehadiran 10%, Tugas 20%, Praktik 40%, UTS 15%, UAS 15%
        val total = (kehadiran * 0.10) +
                (tugas * 0.20) +
                (praktik * 0.40) +
                (uts * 0.15) +
                (uas * 0.15)

        val roundedTotal = String.format(Locale.US, "%.2f", total).toDouble()

        val (huruf, bobot) = when {
            roundedTotal >= 85.0 -> "A" to 4.0
            roundedTotal >= 80.0 -> "A-" to 3.7
            roundedTotal >= 75.0 -> "B+" to 3.3
            roundedTotal >= 70.0 -> "B" to 3.0
            roundedTotal >= 65.0 -> "B-" to 2.7
            roundedTotal >= 60.0 -> "C+" to 2.3
            roundedTotal >= 55.0 -> "C" to 2.0
            roundedTotal >= 45.0 -> "D" to 1.0
            else -> "E" to 0.0
        }

        val status = if (roundedTotal >= 60.0) "Kompeten" else "Belum Kompeten"

        return CalculationResult(
            nilaiAkhir = roundedTotal,
            nilaiHuruf = huruf,
            bobot = bobot,
            statusKelulusan = status
        )
    }

    fun getPredikat(ipk: Double): String {
        return when {
            ipk >= 3.75 -> "Dengan Pujian (Cum Laude)"
            ipk >= 3.50 -> "Sangat Memuaskan"
            ipk >= 3.00 -> "Memuaskan"
            ipk >= 2.50 -> "Cukup"
            else -> "Perlu Bimbingan"
        }
    }
}
