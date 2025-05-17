package com.example.mykisah.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mykisah.ui.components.CategoryDropdownMenu
import com.example.mykisah.ui.viewmodel.TodoViewModel

@Composable
fun TodoSection(
    onTodoClick: (String) -> Unit,
    viewModel: TodoViewModel = viewModel()
) {
    val todos by viewModel.filteredTodos.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val todoToDelete by viewModel.todoToDelete.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            label = { Text("Cari tugas...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Filter options (Active, Completed, All)
        Row(modifier = Modifier.padding(16.dp)) {
            CategoryDropdownMenu(
                categories = listOf("Semua", "Aktif", "Selesai"),
                selected = selectedFilter
            ) {
                viewModel.updateSelectedFilter(it)
            }
        }

        // Todo List
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            items(todos) { todo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onTodoClick(todo.id) }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = todo.isCompleted,
                            onCheckedChange = { viewModel.toggleTodoCompletion(todo) }
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = todo.title,
                                style = MaterialTheme.typography.titleMedium,
                                textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                            )
                            Text(
                                text = todo.description,
                                style = MaterialTheme.typography.bodyMedium,
                                textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                            )
                        }

                        IconButton(
                            onClick = { viewModel.showDeleteConfirmationDialog(todo) }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Todo")
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog && todoToDelete != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            title = { Text("Hapus Tugas?") },
            text = { Text("Yakin mau hapus tugas ini?") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmDeleteTodo() }) {
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