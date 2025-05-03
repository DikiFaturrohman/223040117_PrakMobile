package com.example.mykisah.ui.screen.home

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mykisah.data.local.db.NoteDb
import com.example.mykisah.data.local.models.Note
import com.example.mykisah.navigation.BottomNavTab
import com.example.mykisah.ui.components.BottomNavigationBar
import kotlinx.coroutines.launch

import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNoteClick: (Note) -> Unit,
    onCreateNewNote: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(BottomNavTab.NOTES) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNewNote,
                containerColor = Color(0xFF6200EE)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Catatan",
                    tint = Color.White
                )

            }
        }
        ,
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                BottomNavTab.NOTES -> NotesSection(
                    onNoteClick = onNoteClick,
                    onCreateNewNote = onCreateNewNote
                )
                BottomNavTab.TODO -> TodoSection()
            }
        }
    }
}
