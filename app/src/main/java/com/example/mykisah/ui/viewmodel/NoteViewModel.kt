package com.example.mykisah.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykisah.data.local.dao.NoteDao
import com.example.mykisah.data.local.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteDao: NoteDao): ViewModel() {

    // All notes
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    // Categories
    val categories: Flow<List<String>> = noteDao.getAllCategories()

    // Selected category notes
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Notes filtered by category
    private val _filteredNotes = MutableStateFlow<List<Note>>(emptyList())
    val filteredNotes: StateFlow<List<Note>> = _filteredNotes.asStateFlow()

    // Current note being edited
    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote.asStateFlow()

    // Operation status
    private val _operationStatus = MutableStateFlow<OperationStatus>(OperationStatus.Idle)
    val operationStatus: StateFlow<OperationStatus> = _operationStatus.asStateFlow()

    fun saveNote(note: Note) {
        viewModelScope.launch {
            try {
                _operationStatus.value = OperationStatus.Loading
                noteDao.insertNote(note)
                _operationStatus.value = OperationStatus.Success
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            try {
                _operationStatus.value = OperationStatus.Loading
                noteDao.insertNote(note)
                _operationStatus.value = OperationStatus.Success
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                _operationStatus.value = OperationStatus.Loading
                noteDao.deleteNote(note)
                _operationStatus.value = OperationStatus.Success
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getNoteById(id: String) {
        viewModelScope.launch {
            try {
                _operationStatus.value = OperationStatus.Loading
                val note = noteDao.getNoteById(id)
                _currentNote.value = note
                _operationStatus.value = OperationStatus.Success
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category

        if (category != null) {
            viewModelScope.launch {
                noteDao.getNotesByCategory(category)
                    .catch { e ->
                        _operationStatus.value = OperationStatus.Error(e.message ?: "Unknown error")
                    }
                    .collect { notes ->
                        _filteredNotes.value = notes
                    }
            }
        }
    }

    fun resetOperationStatus() {
        _operationStatus.value = OperationStatus.Idle
    }

    sealed class OperationStatus {
        object Idle : OperationStatus()
        object Loading : OperationStatus()
        object Success : OperationStatus()
        data class Error(val message: String) : OperationStatus()
    }
}