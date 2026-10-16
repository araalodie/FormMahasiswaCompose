package com.example.formmahasiswa.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formmahasiswa.model.Mahasiswa
import kotlinx.coroutines.launch

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
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // ------------------------------------------------------------------------
    // 1. STATE (remember + mutableStateOf)
    // ------------------------------------------------------------------------
    // State input data
    var nama by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var prodi by remember { mutableStateOf("") }

    // State error validasi
    var namaError by remember { mutableStateOf<String?>(null) }
    var nimError by remember { mutableStateOf<String?>(null) }
    var prodiError by remember { mutableStateOf<String?>(null) }

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

    // State untuk Snackbar & CoroutineScope
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // ------------------------------------------------------------------------
    // 2. LOGIKA VALIDASI INPUT
    // ------------------------------------------------------------------------
    fun validateInputs(): Boolean {
        var isValid = true

        // Validasi Nama: tidak boleh kosong dan minimal 3 karakter
        if (nama.isBlank()) {
            namaError = "Nama lengkap wajib diisi"
            isValid = false
        } else if (nama.trim().length < 3) {
            namaError = "Nama minimal harus 3 karakter"
            isValid = false
        } else {
            namaError = null
        }

        // Validasi NIM: tidak boleh kosong, hanya angka, harus tepat 7 digit (contoh: 2411025)
        if (nim.isBlank()) {
            nimError = "NIM wajib diisi"
            isValid = false
        } else if (!nim.all { it.isDigit() }) {
            nimError = "NIM hanya boleh berisi angka"
            isValid = false
        } else if (nim.length != 7) {
            nimError = "NIM harus tepat 7 digit (contoh: 2411025)"
            isValid = false
        } else {
            nimError = null
        }

        // Validasi Program Studi: tidak boleh kosong
        if (prodi.isBlank()) {
            prodiError = "Program Studi wajib dipilih"
            isValid = false
        } else {
            prodiError = null
        }

        return isValid
    }

    // ------------------------------------------------------------------------
    // UI LAYOUT DENGAN SCAFFOLD & SNACKBAR
    // ------------------------------------------------------------------------
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    actionColor = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
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

            // ----------------------------------------------------------------
            // INPUT 1: NAMA LENGKAP
            // ----------------------------------------------------------------
            OutlinedTextField(
                value = nama,
                // EVENT: onValueChange mengupdate State & mereset pesan error
                onValueChange = { newValue ->
                    nama = newValue
                    if (namaError != null) namaError = null
                },
                label = { Text("Nama Lengkap") },
                placeholder = { Text("Contoh: Budi Santoso") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Icon Nama",
                        tint = if (namaError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (namaError != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error Nama",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = namaError != null,
                supportingText = {
                    if (namaError != null) {
                        Text(
                            text = namaError ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Text(
                            text = "${nama.length} karakter",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ----------------------------------------------------------------
            // INPUT 2: NIM (Nomor Induk Mahasiswa)
            // ----------------------------------------------------------------
            OutlinedTextField(
                value = nim,
                // EVENT: onValueChange dibatasi maksimal 7 digit angka
                onValueChange = { newValue ->
                    if (newValue.length <= 7 && newValue.all { it.isDigit() }) {
                        nim = newValue
                        if (nimError != null) nimError = null
                    }
                },
                label = { Text("NIM (7 Digit)") },
                placeholder = { Text("Contoh: 2411025") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = "Icon NIM",
                        tint = if (nimError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (nimError != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error NIM",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = nimError != null,
                supportingText = {
                    if (nimError != null) {
                        Text(
                            text = nimError ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Text(
                            text = "${nim.length}/7 digit angka",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ----------------------------------------------------------------
            // INPUT 3: PROGRAM STUDI (Exposed Dropdown Menu)
            // ----------------------------------------------------------------
            ExposedDropdownMenuBox(
                expanded = isProdiExpanded,
                onExpandedChange = { isProdiExpanded = !isProdiExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = prodi,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Program Studi") },
                    placeholder = { Text("Pilih Program Studi") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Icon Prodi",
                            tint = if (prodiError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isProdiExpanded)
                    },
                    isError = prodiError != null,
                    supportingText = {
                        if (prodiError != null) {
                            Text(
                                text = prodiError ?: "",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = isProdiExpanded,
                    onDismissRequest = { isProdiExpanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    daftarProdi.forEach { pilihanProdi ->
                        DropdownMenuItem(
                            text = { Text(pilihanProdi) },
                            // EVENT: onClick pemilihan item dropdown
                            onClick = {
                                prodi = pilihanProdi
                                prodiError = null
                                isProdiExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ----------------------------------------------------------------
            // ACTION BUTTONS: SIMPAN & RESET
            // ----------------------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tombol Reset
                OutlinedButton(
                    // EVENT: onClick Reset
                    onClick = {
                        nama = ""
                        nim = ""
                        prodi = ""
                        namaError = null
                        nimError = null
                        prodiError = null
                        submittedMahasiswa = null
                        focusManager.clearFocus()

                        // Menampilkan Snackbar info reset
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Formulir telah dibersihkan.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Reset Form",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reset")
                }

                // Tombol Simpan
                Button(
                    // EVENT: onClick Simpan dengan Validasi & Snackbar
                    onClick = {
                        focusManager.clearFocus()
                        val isValid = validateInputs()

                        coroutineScope.launch {
                            if (isValid) {
                                // Memperbarui state submittedMahasiswa (memicu RECOMPOSITION pada Card Hasil)
                                submittedMahasiswa = Mahasiswa(
                                    nama = nama.trim(),
                                    nim = nim.trim(),
                                    prodi = prodi
                                )

                                // Menampilkan Snackbar Berhasil
                                snackbarHostState.showSnackbar(
                                    message = "Berhasil! Data $nama tersimpan.",
                                    actionLabel = "Tutup",
                                    duration = SnackbarDuration.Short
                                )
                            } else {
                                // Menampilkan Snackbar Validasi Gagal
                                snackbarHostState.showSnackbar(
                                    message = "Validasi gagal. Mohon periksa kembali isian Anda.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .weight(1.5f)
                        .height(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Simpan Data",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Simpan Data",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ----------------------------------------------------------------
            // 5. DEMONSTRASI RECOMPOSITION: HASIL OUTPUT CARD
            // ----------------------------------------------------------------
            // Komponen ini akan otomatis di-rekomposisi dan dirender
            // ketika state `submittedMahasiswa` berubah nilainya.
            AnimatedVisibility(
                visible = submittedMahasiswa != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 40 }),
                exit = fadeOut()
            ) {
                val currentMahasiswa = submittedMahasiswa
                if (currentMahasiswa != null) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Status Berhasil",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Data Tersimpan (Recomposition Result)",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                )
                            }

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )

                            // Item Nama
                            DataRowItem(label = "Nama Lengkap", value = currentMahasiswa.nama)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Item NIM
                            DataRowItem(label = "NIM", value = currentMahasiswa.nim)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Item Program Studi
                            DataRowItem(label = "Program Studi", value = currentMahasiswa.prodi)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Komponen pembantu untuk menampilkan baris data tersimpan
 */
@Composable
fun DataRowItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}
