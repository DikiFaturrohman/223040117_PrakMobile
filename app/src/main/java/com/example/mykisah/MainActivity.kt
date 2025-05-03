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
import com.example.mykisah.data.local.models.Note
import com.example.mykisah.ui.screen.home.HomeScreen
import com.example.mykisah.ui.screen.home.NoteScreen
import com.example.mykisah.ui.theme.MyKisahTheme

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

@Composable
fun AppNavigator() {
    var currentScreen by remember { mutableStateOf("home") }
    var selectedNote by remember { mutableStateOf<Note?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        when (currentScreen) {
            "home" -> HomeScreen(
                modifier = Modifier.padding(paddingValues),
                onNoteClick = { note ->
                    selectedNote = note
                    currentScreen = "note"
                },
                onCreateNewNote = {
                    selectedNote = null
                    currentScreen = "note"
                }
            )

            "note" -> NoteScreen(
                note = selectedNote,
                navBack = {
                    currentScreen = "home"
                    selectedNote = null
                }
            )
        }
    }
}
