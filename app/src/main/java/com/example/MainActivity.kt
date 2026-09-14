package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.components.AppBottomBar
import com.example.ui.components.AppHeader
import com.example.ui.components.AppNavRail
import com.example.ui.screens.AnalitikScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DosenScreen
import com.example.ui.screens.LaporanScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NilaiScreen
import com.example.ui.screens.NotifikasiScreen
import com.example.ui.screens.PelajaranScreen
import com.example.ui.screens.SiswaScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: MainViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val staffName by viewModel.currentUsername.collectAsState()
    val loginError by viewModel.loginError.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()

    val dosenList by viewModel.dosenList.collectAsState()
    val siswaList by viewModel.siswaList.collectAsState()
    val pelajaranList by viewModel.pelajaranList.collectAsState()
    val nilaiList by viewModel.nilaiList.collectAsState()
    val nilaiDetailList by viewModel.nilaiDetailList.collectAsState()
    val notifikasiList by viewModel.notifikasiList.collectAsState()
    val analyticsData by viewModel.analyticsData.collectAsState()

    val selectedStudentIdForReport by viewModel.selectedStudentIdForReport.collectAsState()
    val selectedSemesterForReport by viewModel.selectedSemesterForReport.collectAsState()

    val pendingNotifCount = notifikasiList.count { it.status == "Pending" }

    if (!isLoggedIn) {
        LoginScreen(
            errorMessage = loginError,
            onLogin = { u, p -> viewModel.login(u, p) },
            onQuickLogin = { viewModel.quickLogin() }
        )
    } else {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isTabletOrWide = maxWidth >= 720.dp

            Row(modifier = Modifier.fillMaxSize()) {
                if (isTabletOrWide) {
                    AppNavRail(
                        currentScreen = currentScreen,
                        onNavigate = { viewModel.navigateTo(it) },
                        pendingNotificationCount = pendingNotifCount
                    )
                }

                Scaffold(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.background),
                    topBar = {
                        AppHeader(
                            currentScreen = currentScreen,
                            staffName = staffName,
                            pendingNotificationCount = pendingNotifCount,
                            onNotificationClick = { viewModel.navigateTo(AppScreen.NOTIFIKASI) },
                            onLogoutClick = { viewModel.logout() }
                        )
                    },
                    bottomBar = {
                        if (!isTabletOrWide) {
                            AppBottomBar(
                                currentScreen = currentScreen,
                                onNavigate = { viewModel.navigateTo(it) },
                                pendingNotificationCount = pendingNotifCount
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            AppScreen.DASHBOARD -> DashboardScreen(
                                staffName = staffName,
                                analytics = analyticsData,
                                upcomingReminders = notifikasiList,
                                onNavigate = { viewModel.navigateTo(it) }
                            )

                            AppScreen.DOSEN -> DosenScreen(
                                dosenList = dosenList,
                                onAddDosen = { viewModel.addDosen(it) },
                                onUpdateDosen = { viewModel.updateDosen(it) },
                                onDeleteDosen = { viewModel.deleteDosen(it) }
                            )

                            AppScreen.SISWA -> SiswaScreen(
                                siswaList = siswaList,
                                onAddSiswa = { viewModel.addSiswa(it) },
                                onUpdateSiswa = { viewModel.updateSiswa(it) },
                                onDeleteSiswa = { viewModel.deleteSiswa(it) },
                                onViewKhs = { siswa ->
                                    viewModel.selectStudentForReport(siswa.id, siswa.semester)
                                    viewModel.navigateTo(AppScreen.LAPORAN)
                                }
                            )

                            AppScreen.PELAJARAN -> PelajaranScreen(
                                pelajaranList = pelajaranList,
                                dosenList = dosenList,
                                onAddPelajaran = { viewModel.addPelajaran(it) },
                                onUpdatePelajaran = { viewModel.updatePelajaran(it) },
                                onDeletePelajaran = { viewModel.deletePelajaran(it) }
                            )

                            AppScreen.NILAI -> NilaiScreen(
                                siswaList = siswaList,
                                pelajaranList = pelajaranList,
                                nilaiList = nilaiList,
                                nilaiDetailList = nilaiDetailList,
                                onSaveNilai = { id, sId, pId, sem, keh, tug, prak, uts, uas ->
                                    viewModel.saveOrUpdateNilai(id, sId, pId, sem, keh, tug, prak, uts, uas)
                                },
                                onDeleteNilai = { viewModel.deleteNilai(it) },
                                onNavigateToLaporan = { studentId, sem ->
                                    viewModel.selectStudentForReport(studentId, sem)
                                    viewModel.navigateTo(AppScreen.LAPORAN)
                                }
                            )

                            AppScreen.LAPORAN -> LaporanScreen(
                                siswaList = siswaList,
                                nilaiDetailList = nilaiDetailList,
                                selectedStudentId = selectedStudentIdForReport,
                                selectedSemester = selectedSemesterForReport,
                                onSelectStudent = { sId, sem -> viewModel.selectStudentForReport(sId, sem) },
                                onSetSemester = { viewModel.setSemesterForReport(it) },
                                onPrintDocument = { context -> viewModel.printCurrentReport(context) },
                                onShareDocument = { context -> viewModel.shareCurrentReport(context) }
                            )

                            AppScreen.NOTIFIKASI -> NotifikasiScreen(
                                notifikasiList = notifikasiList,
                                onAddNotifikasi = { viewModel.addNotifikasi(it) },
                                onToggleStatus = { viewModel.toggleNotifikasiStatus(it) },
                                onDeleteNotifikasi = { viewModel.deleteNotifikasi(it) },
                                onTriggerNotification = { ctx, notif ->
                                    viewModel.sendSystemNotification(ctx, notif)
                                }
                            )

                            AppScreen.ANALITIK -> AnalitikScreen(
                                analytics = analyticsData
                            )
                        }
                    }
                }
            }
        }
    }
}
