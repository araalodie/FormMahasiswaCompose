package com.example.formmahasiswa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.formmahasiswa.model.Mahasiswa

/**
 * Screen utama untuk input data Mahasiswa.
 * Mengimplementasikan:
 * 1. State (remember & mutableStateOf)
 * 2. Event (onValueChange, onClick)
 * 3. Validasi Input (Nama, NIM, Program Studi)
 * 4. Snackbar (SnackbarHostState & CoroutineScope)
 * 5. Recomposition (UI otomatis diperbarui saat State berubah)
 * 6. Tema Gelap (Dark Theme)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahasiswaFormScreen() {
    val scrollState = rememberScrollState()

    // ------------------------------------------------------------------------
    // 1. STATE (remember + mutableStateOf)
    // ------------------------------------------------------------------------
    // State input data
    var nama by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var prodi by remember { mutableStateOf("") }

    // State data mahasiswa yang berhasil disubmit (Recomposition trigger)
    var submittedMahasiswa by remember { mutableStateOf<Mahasiswa?>(null) }

    // State untuk dropdown Program Studi
    var isProdiExpanded by remember { mutableStateOf(false) }
    val daftarProdi = listOf(
        "Informatika",
        "Sistem Informasi",
        "Teknik Elektro",
        "Teknologi Informasi",
        "Ilmu Komputer"
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Judul
            Text(
                text = "Formulir Mahasiswa",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "Input data dengan State & Recomposition Compose",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )
        }
    }
}
