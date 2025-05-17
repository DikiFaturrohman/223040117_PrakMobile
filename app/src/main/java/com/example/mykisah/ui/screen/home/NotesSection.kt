package com.example.mykisah.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mykisah.data.local.models.Note
import com.example.mykisah.ui.components.CategoryDropdownMenu
import com.example.mykisah.ui.viewmodel.NotesViewModel

@Composable
fun NotesSection(
    onNoteClick: (Note) -> Unit,
    onCreateNewNote: () -> Unit,
    viewModel: NotesViewModel = viewModel()
) {
    val filteredNotes by viewModel.filteredNotes.collectAsState()
    val allCategories by viewModel.allCategories.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val noteToDelete by viewModel.noteToDelete.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateNewNote) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Catatan")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Cari catatan...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Category Filter
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kategori:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 8.dp)
                )

                CategoryDropdownMenu(
                    categories = listOf("Semua") + allCategories,
                    selected = selectedCategory,
                    onValueChange = { viewModel.updateSelectedCategory(it) }
                )
            }

            // Notes List
            if (filteredNotes.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Tidak ada catatan ditemukan",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(filteredNotes) { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onNoteClick(note) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(note.title, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        note.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 2
                                    )
                                    Text(
                                        "Kategori: ${note.category}",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.showDeleteConfirmationDialog(note) }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Note")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog && noteToDelete != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            title = { Text("Hapus Catatan?") },
            text = { Text("Yakin mau hapus catatan ini?") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmDeleteNote() }) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                    Text("Batal")
                }
            }
        )
    }
}