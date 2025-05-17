package com.example.mykisah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mykisah.data.local.models.Note
import com.example.mykisah.ui.screen.home.HomeScreen
import com.example.mykisah.ui.screen.home.NoteScreen
import com.example.mykisah.ui.screen.home.TodoDetailScreen
import com.example.mykisah.ui.theme.MyKisahTheme
import com.example.mykisah.ui.viewmodel.NotesViewModel
import com.example.mykisah.ui.viewmodel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyKisahTheme {
                AppNavigator()
            }
        }
    }
}

sealed class Screen {
    object Home : Screen()
    data class NoteDetail(val noteId: String? = null) : Screen()
    data class TodoDetail(val todoId: String) : Screen()
}

@Composable
fun AppNavigator() {
    val notesViewModel: NotesViewModel = viewModel()
    val todoViewModel: TodoViewModel = viewModel()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        when (val screen = currentScreen) {
            is Screen.Home -> HomeScreen(
                modifier = Modifier.padding(paddingValues),
                onNoteClick = { note ->
                    currentScreen = Screen.NoteDetail(note.id)
                },
                onCreateNewNote = {
                    currentScreen = Screen.NoteDetail(null)
                },
                onTodoClick = { todoId ->
                    currentScreen = Screen.TodoDetail(todoId)
                },
                notesViewModel = notesViewModel,
                todoViewModel = todoViewModel
            )

            is Screen.NoteDetail -> NoteScreen(
                noteId = screen.noteId,
                navBack = {
                    currentScreen = Screen.Home
                }
            )

            is Screen.TodoDetail -> TodoDetailScreen(
                todoId = screen.todoId,
                navBack = {
                    currentScreen = Screen.Home
                },
                todoViewModel = todoViewModel
            )
        }
    }
}