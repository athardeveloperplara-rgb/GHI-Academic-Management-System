package com.example.data.util

import android.content.Context
import android.content.Intent
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.data.model.NilaiDetail
import com.example.data.model.Siswa
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PrintHelper {

    fun generateHtmlTranscript(
        siswa: Siswa,
        semester: Int,
        nilaiList: List<NilaiDetail>,
        totalSks: Int,
        ips: Double,
        ipk: Double,
        predikat: String
    ): String {
        val today = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())

        val tableRows = StringBuilder()
        nilaiList.forEachIndexed { index, item ->
            val n = item.nilai
            tableRows.append(
                """
                <tr>
                    <td style="text-align:center;">${index + 1}</td>
                    <td style="text-align:center;"><b>${item.pelajaranKode}</b></td>
                    <td>${item.pelajaranNama}<br><small style="color:#555;">Dosen: ${item.dosenNama} (${item.pelajaranKategori})</small></td>
                    <td style="text-align:center;">${item.pelajaranSks}</td>
                    <td style="text-align:center;">${n.nilaiPraktik}</td>
                    <td style="text-align:center;">${n.nilaiAkhir}</td>
                    <td style="text-align:center;"><b>${n.nilaiHuruf}</b></td>
                    <td style="text-align:center;">${n.bobot}</td>
                    <td style="text-align:center; color:${if (n.statusKelulusan == "Kompeten") "#2D6A4F" else "#D90429"}; font-weight:bold;">
                        ${n.statusKelulusan}
                    </td>
                </tr>
                """.trimIndent()
            )
        }

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>KHS - ${siswa.nim} - ${siswa.nama}</title>
            <style>
                body {
                    font-family: 'Helvetica Neue', Arial, sans-serif;
                    color: #1A202C;
                    margin: 20px;
                    background-color: #FFFFFF;
                }
                .header {
                    border-bottom: 3px double #0F2B48;
                    padding-bottom: 12px;
                    margin-bottom: 20px;
                    text-align: center;
                }
                .header h1 {
                    margin: 0;
                    font-size: 20pt;
                    color: #0F2B48;
                    letter-spacing: 1px;
                }
                .header h2 {
                    margin: 4px 0 0 0;
                    font-size: 13pt;
                    color: #C5A059;
                    font-weight: 600;
                }
                .header p {
                    margin: 4px 0 0 0;
                    font-size: 8.5pt;
                    color: #4A5568;
                }
                .title-doc {
                    text-align: center;
                    margin: 16px 0;
                }
                .title-doc h3 {
                    margin: 0;
                    font-size: 14pt;
                    text-decoration: underline;
                    color: #0F2B48;
                }
                .student-info {
                    width: 100%;
                    margin-bottom: 15px;
                    font-size: 9.5pt;
                }
                .student-info td {
                    padding: 3px 0;
                }
                table.grades {
                    width: 100%;
                    border-collapse: collapse;
                    font-size: 9pt;
                    margin-bottom: 16px;
                }
                table.grades th {
                    background-color: #0F2B48;
                    color: #FFFFFF;
                    border: 1px solid #0F2B48;
                    padding: 7px 4px;
                    font-weight: bold;
                    text-align: center;
                }
                table.grades td {
                    border: 1px solid #CBD5E1;
                    padding: 6px 5px;
                }
                table.grades tr:nth-child(even) {
                    background-color: #F8FAFC;
                }
                .summary-box {
                    background-color: #F1F5F9;
                    border: 1px solid #CBD5E1;
                    border-radius: 4px;
                    padding: 10px;
                    margin-bottom: 20px;
                    font-size: 9.5pt;
                }
                .signatures {
                    width: 100%;
                    margin-top: 30px;
                    font-size: 9pt;
                }
                .signatures td {
                    text-align: center;
                    vertical-align: top;
                    width: 50%;
                }
                .sign-space {
                    height: 65px;
                }
            </style>
        </head>
        <body>
            <div class="header">
                <h1>LKP GRAND HOSPITALITY INSTITUTE</h1>
                <h2>LEMBAGA KURSUS DAN PELATIHAN PERHOTELAN TERAKREDITASI</h2>
                <p>Izin Kemendikbudristek & Kemenaker RI | Akreditasi Lembaga Predikat A (Unggul)</p>
                <p>Kampus Pusat: Grand Hospitality Boulevard No. 88, Suite 400 | Telp: (021) 7890-8888 | info@grandhospitality.ac.id</p>
            </div>

            <div class="title-doc">
                <h3>KARTU HASIL STUDI (KHS) / LAPORAN AKADEMIK</h3>
                <span style="font-size:9.5pt; color:#4A5568;">Tahun Akademik 2026/2027</span>
            </div>

            <table class="student-info">
                <tr>
                    <td style="width:18%;"><b>Nomor Induk (NIM)</b></td>
                    <td style="width:2%;">:</td>
                    <td style="width:30%;">${siswa.nim}</td>
                    <td style="width:18%;"><b>Jurusan Perhotelan</b></td>
                    <td style="width:2%;">:</td>
                    <td style="width:30%;">${siswa.jurusan}</td>
                </tr>
                <tr>
                    <td><b>Nama Siswa</b></td>
                    <td>:</td>
                    <td><b>${siswa.nama}</b></td>
                    <td><b>Semester / Angkatan</b></td>
                    <td>:</td>
                    <td>Semester $semester / ${siswa.angkatan}</td>
                </tr>
                <tr>
                    <td><b>Status Akademik</b></td>
                    <td>:</td>
                    <td>${siswa.statusAkademik}</td>
                    <td><b>Status SPP / Keuangan</b></td>
                    <td>:</td>
                    <td><span style="color:${if (siswa.statusSpp == "Lunas") "#2D6A4F" else "#D90429"}; font-weight:bold;">${siswa.statusSpp}</span></td>
                </tr>
            </table>

            <table class="grades">
                <thead>
                    <tr>
                        <th style="width:5%;">No</th>
                        <th style="width:12%;">Kode</th>
                        <th>Mata Kuliah / Pelajaran</th>
                        <th style="width:6%;">SKS</th>
                        <th style="width:10%;">Praktik (40%)</th>
                        <th style="width:10%;">Nilai Akhir</th>
                        <th style="width:8%;">Huruf</th>
                        <th style="width:8%;">Bobot</th>
                        <th style="width:15%;">Status</th>
                    </tr>
                </thead>
                <tbody>
                    $tableRows
                </tbody>
            </table>

            <div class="summary-box">
                <table style="width:100%; border:none;">
                    <tr>
                        <td style="width:25%;"><b>Total Beban SKS</b>: $totalSks SKS</td>
                        <td style="width:25%;"><b>Indeks Semester (IPS)</b>: <b>$ips</b></td>
                        <td style="width:25%;"><b>Indeks Kumulatif (IPK)</b>: <b>$ipk</b></td>
                        <td style="width:25%;"><b>Predikat</b>: <span style="color:#0F2B48; font-weight:bold;">$predikat</span></td>
                    </tr>
                </table>
            </div>

            <table class="signatures">
                <tr>
                    <td>
                        Mengetahui,<br>
                        <b>Kepala Bagian Akademik & Administrasi</b>
                        <div class="sign-space"></div>
                        <u><b>Dra. Hj. Sri Wahyuni, M.M.</b></u><br>
                        NIP. GHI-ADM-198504-001
                    </td>
                    <td>
                        Jakarta, $today<br>
                        <b>Direktur Utama LKP Grand Hospitality</b>
                        <div class="sign-space"></div>
                        <u><b>Dr. H. Hendra Wijaya, CHE., CHA.</b></u><br>
                        NIDN. 8829103901
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """.trimIndent()
    }

    fun printDocument(context: Context, htmlContent: String, jobName: String) {
        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                val printAdapter = webView.createPrintDocumentAdapter(jobName)
                val printAttributes = PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(PrintAttributes.Resolution("ghi_res", "Print Resolution", 300, 300))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                    .build()
                printManager?.print(jobName, printAdapter, printAttributes)
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    fun generateShareableText(
        siswa: Siswa,
        semester: Int,
        nilaiList: List<NilaiDetail>,
        totalSks: Int,
        ips: Double,
        ipk: Double,
        predikat: String
    ): String {
        val sb = java.lang.StringBuilder()
        sb.append("========================================\n")
        sb.append("    LKP GRAND HOSPITALITY INSTITUTE     \n")
        sb.append("   KARTU HASIL STUDI (KHS) RESMI       \n")
        sb.append("========================================\n")
        sb.append("NIM       : ${siswa.nim}\n")
        sb.append("Nama      : ${siswa.nama}\n")
        sb.append("Jurusan   : ${siswa.jurusan}\n")
        sb.append("Semester  : $semester (${siswa.angkatan})\n")
        sb.append("Status SPP: ${siswa.statusSpp}\n")
        sb.append("----------------------------------------\n")
        sb.append("DAFTAR NILAI MATA KULIAH:\n")
        nilaiList.forEachIndexed { i, it ->
            sb.append("${i + 1}. [${it.pelajaranKode}] ${it.pelajaranNama}\n")
            sb.append("   SKS: ${it.pelajaranSks} | Nilai: ${it.nilai.nilaiAkhir} (${it.nilai.nilaiHuruf}) | Status: ${it.nilai.statusKelulusan}\n")
        }
        sb.append("----------------------------------------\n")
        sb.append("Total SKS           : $totalSks SKS\n")
        sb.append("Indeks Semester(IPS): $ips\n")
        sb.append("Indeks Kumulatif(IPK: $ipk\n")
        sb.append("Predikat Akademik   : $predikat\n")
        sb.append("========================================\n")
        sb.append("Dokumen Sah Administrasi LKP Grand Hospitality Institute\n")
        return sb.toString()
    }

    fun shareTranscript(context: Context, content: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Laporan Akademik KHS - LKP Grand Hospitality Institute")
            putExtra(Intent.EXTRA_TEXT, content)
        }
        context.startActivity(Intent.createChooser(intent, "Bagikan Laporan Akademik"))
    }
}
