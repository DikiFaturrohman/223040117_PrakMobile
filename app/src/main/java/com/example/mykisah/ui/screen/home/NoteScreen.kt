package com.example.mykisah.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mykisah.data.local.db.NoteDb
import com.example.mykisah.data.local.models.Note
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun NoteScreen(
    note: Note?, // data catatan yang akan diedit, null = catatan baru
    navBack: () -> Unit
) {
    val context = LocalContext.current
    val dao = NoteDb.getDatabase(context).noteDao()

    var title by remember { mutableStateOf(note?.title ?: "") }
    var description by remember { mutableStateOf(note?.description ?: "") }

    val scope = rememberCoroutineScope()

    fun safelySaveNoteAndBack() {
        scope.launch {
            try {
                if (title.isNotBlank() && description.isNotBlank()) {
                    val updatedNote = if (note != null && note.id.isNotBlank()) {
                        note.copy(title = title, description = description)
                    } else {
                        Note(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            description = description
                        )
                    }
                    dao.insertNote(updatedNote)
                }
            } catch (e: Exception) {
                e.printStackTrace() // log error ke Logcat
            } finally {
                navBack() // Tetap kembali meski error
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = { safelySaveNoteAndBack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali"
            )
        }

        Spacer(Modifier.height(16.dp))

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Isi Catatan") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { safelySaveNoteAndBack() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Simpan Catatan")
        }
    }
}
