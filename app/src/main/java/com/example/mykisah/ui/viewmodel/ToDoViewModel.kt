package com.example.mykisah.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykisah.data.local.db.TodoDb
import com.example.mykisah.data.local.models.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    // Asumsikan kita memiliki TodoDao dan Todo model (perlu dibuat jika belum ada)
    private val todoDao = TodoDb.getDatabase(application).todoDao()

    // State untuk todo items
    private val _allTodos = MutableStateFlow<List<Todo>>(emptyList())
    val allTodos: StateFlow<List<Todo>> = _allTodos.asStateFlow()

    // State untuk filter (misalnya active, completed, all)
    private val _selectedFilter = MutableStateFlow("Semua")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    // State untuk pencarian
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // State untuk todo yang difilter
    private val _filteredTodos = MutableStateFlow<List<Todo>>(emptyList())
    val filteredTodos: StateFlow<List<Todo>> = _filteredTodos.asStateFlow()

    // State untuk dialog hapus
    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    // State untuk todo yang akan dihapus
    private val _todoToDelete = MutableStateFlow<Todo?>(null)
    val todoToDelete: StateFlow<Todo?> = _todoToDelete.asStateFlow()

    init {
        loadTodos()
        applyFilters()
    }

    // Untuk mendapatkan todo berdasarkan ID
    suspend fun getTodoById(id: String): Todo? {
        return todoDao.getTodoById(id)
    }

    private fun loadTodos() {
        viewModelScope.launch {
            todoDao.getAllTodos().collect { todos ->
                _allTodos.value = todos
                applyFilters()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun updateSelectedFilter(filter: String) {
        _selectedFilter.value = filter
        applyFilters()
    }

    private fun applyFilters() {
        val filtered = _allTodos.value.filter { todo ->
            when (selectedFilter.value) {
                "Aktif" -> !todo.isCompleted
                "Selesai" -> todo.isCompleted
                else -> true // "Semua"
            } && (todo.title.contains(searchQuery.value, ignoreCase = true) ||
                    todo.description.contains(searchQuery.value, ignoreCase = true))
        }
        _filteredTodos.value = filtered
    }

    fun toggleTodoCompletion(todo: Todo) {
        viewModelScope.launch {
            val updatedTodo = todo.copy(isCompleted = !todo.isCompleted)
            todoDao.updateTodo(updatedTodo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.deleteTodo(todo)
        }
    }

    fun showDeleteConfirmationDialog(todo: Todo) {
        _todoToDelete.value = todo
        _showDeleteDialog.value = true
    }

    fun dismissDeleteDialog() {
        _showDeleteDialog.value = false
        _todoToDelete.value = null
    }

    fun confirmDeleteTodo() {
        _todoToDelete.value?.let { todo ->
            deleteTodo(todo)
            dismissDeleteDialog()
        }
    }

    fun addTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.insertTodo(todo)
        }
    }

    // Untuk menyimpan atau memperbarui todo
    fun saveTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.insertTodo(todo)
        }
    }
}