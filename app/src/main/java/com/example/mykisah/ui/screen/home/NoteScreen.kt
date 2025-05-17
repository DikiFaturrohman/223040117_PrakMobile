package com.example.mykisah.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mykisah.data.local.models.Note
import com.example.mykisah.ui.components.CategoryDropdownMenu
import com.example.mykisah.ui.viewmodel.NoteViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    noteId: String?,
    navBack: () -> Unit,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val currentNote by viewModel.currentNote.collectAsState()
    val categories by viewModel.categories.collectAsState(initial = emptyList())
    val operationStatus by viewModel.operationStatus.collectAsState()

    // Load note if we have an ID
    if (noteId != null && currentNote == null) {
        viewModel.getNoteById(noteId)
    }

    var title by remember(currentNote) { mutableStateOf(currentNote?.title ?: "") }
    var description by remember(currentNote) { mutableStateOf(currentNote?.description ?: "") }
    var category by remember(currentNote) { mutableStateOf(currentNote?.category ?: "") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val isNewNote = noteId == null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNewNote) "Catatan Baru" else "Edit Catatan") },
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    if (!isNewNote) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val updatedNote = if (isNewNote) {
                    Note(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        category = category
                    )
                } else {
                    currentNote!!.copy(
                        title = title,
                        description = description,
                        category = category
                    )
                }

                scope.launch {
                    if (isNewNote) {
                        viewModel.saveNote(updatedNote)
                    } else {
                        viewModel.updateNote(updatedNote)
                    }
                    navBack()
                }
            }) {
                Icon(Icons.Default.Check, contentDescription = "Simpan")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Kategori:",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            CategoryDropdownMenu(
                categories = categories,
                selected = category,
                allowCustom = true,
                onValueChange = { category = it }
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Catatan?") },
            text = { Text("Yakin mau hapus catatan ini?") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        currentNote?.let { viewModel.deleteNote(it) }
                        navBack()
                    }
                }) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}