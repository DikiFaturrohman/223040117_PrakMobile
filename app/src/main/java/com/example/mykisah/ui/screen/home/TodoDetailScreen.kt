package com.example.mykisah.ui.screen.home


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mykisah.data.local.models.Todo
import com.example.mykisah.ui.viewmodel.TodoViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    todoId: String,
    navBack: () -> Unit,
    todoViewModel: TodoViewModel
) {
    val scope = rememberCoroutineScope()
    var todo by remember { mutableStateOf<Todo?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isCompleted by remember { mutableStateOf(false) }
    var priority by remember { mutableStateOf(0) } // 0:Low, 1:Medium, 2:High
    var deadline by remember { mutableStateOf<Long?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val isNewTodo = todoId.isEmpty()

    // Load todo if it's not a new one
    LaunchedEffect(todoId) {
        if (!isNewTodo) {
            todo = todoViewModel.getTodoById(todoId)
            todo?.let {
                title = it.title
                description = it.description
                isCompleted = it.isCompleted
                priority = it.priority
                deadline = it.deadline
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNewTodo) "Tugas Baru" else "Edit Tugas") },
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    if (!isNewTodo) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val updatedTodo = if (isNewTodo) {
                    Todo(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        isCompleted = isCompleted,
                        priority = priority,
                        deadline = deadline
                    )
                } else {
                    todo!!.copy(
                        title = title,
                        description = description,
                        isCompleted = isCompleted,
                        priority = priority,
                        deadline = deadline
                    )
                }

                scope.launch {
                    todoViewModel.saveTodo(updatedTodo)
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

            // Status (isCompleted)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { isCompleted = it }
                )
                Text("Selesai")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Priority
            Text("Prioritas:", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = priority == 0,
                    onClick = { priority = 0 },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                ) {
                    Text("Rendah")
                }
                SegmentedButton(
                    selected = priority == 1,
                    onClick = { priority = 1 },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                ) {
                    Text("Sedang")
                }
                SegmentedButton(
                    selected = priority == 2,
                    onClick = { priority = 2 },
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                ) {
                    Text("Tinggi")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Deadline
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    deadline?.let { "Tenggat: ${Date(it).toLocaleString()}" }
                        ?: "Pilih Tenggat"
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        deadline = it
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Tugas?") },
            text = { Text("Yakin mau hapus tugas ini?") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        todo?.let { todoViewModel.deleteTodo(it) }
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