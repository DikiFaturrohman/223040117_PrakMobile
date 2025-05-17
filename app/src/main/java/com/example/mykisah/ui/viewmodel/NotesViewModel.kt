package com.example.mykisah.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykisah.data.local.db.NoteDb
import com.example.mykisah.data.local.models.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.room.Room

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    // Membuat instance database secara langsung
    private val noteDb = Room.databaseBuilder(
        application.applicationContext,
        NoteDb::class.java,
        "notes_database"
    ).fallbackToDestructiveMigration().build()

    private val noteDao = noteDb.noteDao()

    // State untuk notes
    private val _allNotes = MutableStateFlow<List<Note>>(emptyList())
    val allNotes: StateFlow<List<Note>> = _allNotes.asStateFlow()

    // State untuk kategori
    private val _allCategories = MutableStateFlow<List<String>>(emptyList())
    val allCategories: StateFlow<List<String>> = _allCategories.asStateFlow()

    // State untuk pencarian
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // State untuk filter kategori
    private val _selectedCategory = MutableStateFlow("Semua")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // State untuk note yang difilter
    private val _filteredNotes = MutableStateFlow<List<Note>>(emptyList())
    val filteredNotes: StateFlow<List<Note>> = _filteredNotes.asStateFlow()

    // State untuk dialog hapus
    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    // State untuk note yang akan dihapus
    private val _noteToDelete = MutableStateFlow<Note?>(null)
    val noteToDelete: StateFlow<Note?> = _noteToDelete.asStateFlow()

    init {
        loadNotes()
        loadCategories()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            noteDao.getAllNotes().collect { notes: List<Note> ->
                _allNotes.value = notes
                applyFilters()
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            noteDao.getAllCategories().collect { categories: List<String> ->
                _allCategories.value = categories
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category
        applyFilters()
    }

    private fun applyFilters() {
        val filtered = _allNotes.value.filter { note ->
            (selectedCategory.value == "Semua" || note.category == selectedCategory.value) &&
                    (note.title.contains(searchQuery.value, ignoreCase = true) ||
                            note.description.contains(searchQuery.value, ignoreCase = true))
        }
        _filteredNotes.value = filtered
    }

    suspend fun getNoteById(id: String): Note? {
        return noteDao.getNoteById(id)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

    fun showDeleteConfirmationDialog(note: Note) {
        _noteToDelete.value = note
        _showDeleteDialog.value = true
    }

    fun dismissDeleteDialog() {
        _showDeleteDialog.value = false
        _noteToDelete.value = null
    }

    fun confirmDeleteNote() {
        _noteToDelete.value?.let { note ->
            deleteNote(note)
            dismissDeleteDialog()
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            noteDao.insertNote(note)
        }
    }
}