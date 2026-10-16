package com.example.formmahasiswa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    val focusManager = LocalFocusManager.current
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

            // ----------------------------------------------------------------
            // INPUT 1: NAMA LENGKAP
            // ----------------------------------------------------------------
            OutlinedTextField(
                value = nama,
                // EVENT: onValueChange mengupdate State & mereset pesan error
                onValueChange = { newValue ->
                    nama = newValue
                },
                label = { Text("Nama Lengkap") },
                placeholder = { Text("Contoh: Budi Santoso") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Icon Nama",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                supportingText = {
                    Text(
                        text = "${nama.length} karakter",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                    }
                },
                label = { Text("NIM (7 Digit)") },
                placeholder = { Text("Contoh: 2411025") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = "Icon NIM",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                supportingText = {
                    Text(
                        text = "${nim.length}/7 digit angka",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isProdiExpanded)
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
                                isProdiExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
