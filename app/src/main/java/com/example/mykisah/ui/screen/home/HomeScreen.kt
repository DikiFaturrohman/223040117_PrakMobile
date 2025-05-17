package com.example.mykisah.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mykisah.data.local.models.Note
import com.example.mykisah.ui.viewmodel.NotesViewModel
import com.example.mykisah.ui.viewmodel.TodoViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNoteClick: (Note) -> Unit,
    onCreateNewNote: () -> Unit,
    onTodoClick: (String) -> Unit,
    notesViewModel: NotesViewModel,
    todoViewModel: TodoViewModel
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Catatan", "Tugas")

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> NotesSection(
                onNoteClick = onNoteClick,
                onCreateNewNote = onCreateNewNote,
                viewModel = notesViewModel
            )
            1 -> TodoSection(
                onTodoClick = onTodoClick,
                viewModel = todoViewModel
            )
        }
    }
}