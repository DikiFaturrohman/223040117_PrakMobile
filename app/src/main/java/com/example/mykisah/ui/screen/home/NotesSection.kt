package com.example.mykisah.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mykisah.data.local.db.NoteDb
import com.example.mykisah.data.local.models.Note
import kotlinx.coroutines.launch


@Composable
fun NotesSection(
    onNoteClick: (Note) -> Unit,
    onCreateNewNote: () -> Unit
) {
    val context = LocalContext.current
    val dao = NoteDb.getDatabase(context).noteDao()
    val notes by dao.getAllNotes().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredNotes = notes.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    )
    {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Cari catatan...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // List Catatan
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
                            Text(note.description, style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(
                            onClick = {
                                noteToDelete = note
                                showDialog = true
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Note")
                        }
                    }
                }
            }
        }

        // Tombol tambah catatan
//        Button(
//            onClick = onCreateNewNote,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            shape = RoundedCornerShape(24.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
//        ) {
//            Text("Buat Catatan Baru", style = MaterialTheme.typography.titleMedium)
//        }
    }

    // Dialog hapus
    if (showDialog && noteToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Hapus Catatan?") },
            text = { Text("Yakin mau hapus catatan ini?") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        dao.deleteNote(noteToDelete!!)
                        showDialog = false
                    }
                }) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Batal") }
            }
        )
    }
}
