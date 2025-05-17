package com.example.mykisah.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykisah.data.local.dao.NoteDao
import com.example.mykisah.data.local.models.Note
import com.example.mykisah.navigation.BottomNavTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for HomeScreen - manages notes data and screen state
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

    // All notes
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    // Categories for filtering
    val categories: Flow<List<String>> = noteDao.getAllCategories()

    // Current selected tab (NOTES or TODO)
    private val _selectedTab = MutableStateFlow(BottomNavTab.NOTES)
    val selectedTab: StateFlow<BottomNavTab> = _selectedTab.asStateFlow()

    // Currently selected category filter
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Notes filtered by category
    private val _filteredNotes = MutableStateFlow<List<Note>>(emptyList())
    val filteredNotes: StateFlow<List<Note>> = _filteredNotes.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Change the currently selected bottom navigation tab
     */
    fun selectTab(tab: BottomNavTab) {
        _selectedTab.value = tab
    }

    /**
     * Filter notes by category
     */
    fun filterByCategory(category: String?) {
        _selectedCategory.value = category

        if (category != null) {
            viewModelScope.launch {
                _isLoading.value = true
                noteDao.getNotesByCategory(category)
                    .catch { e ->
                        _error.value = e.message ?: "Error loading notes"
                        _isLoading.value = false
                    }
                    .collect { notes ->
                        _filteredNotes.value = notes
                        _isLoading.value = false
                    }
            }
        } else {
            // Reset to show all notes
            viewModelScope.launch {
                _isLoading.value = true
                allNotes
                    .catch { e ->
                        _error.value = e.message ?: "Error loading notes"
                        _isLoading.value = false
                    }
                    .collect { notes ->
                        _filteredNotes.value = notes
                        _isLoading.value = false
                    }
            }
        }
    }

    /**
     * Delete a note
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                noteDao.deleteNote(note)
                // Refresh filtered notes if a category is selected
                _selectedCategory.value?.let { filterByCategory(it) }
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Error deleting note"
                _isLoading.value = false
            }
        }
    }

    /**
     * Clear any error message
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Search notes by title or content
     */
    fun searchNotes(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // This would need a DAO method to search notes
                // For now, we'll filter in memory from all notes
                allNotes.collect { notesList ->
                    if (query.isBlank()) {
                        _filteredNotes.value = notesList
                    } else {
                        _filteredNotes.value = notesList.filter { note ->
                            note.title.contains(query, ignoreCase = true) ||
                                    note.description.contains(query, ignoreCase = true)
                        }
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error searching notes"
                _isLoading.value = false
            }
        }
    }

    /**
     * Initialize the ViewModel by loading all notes
     */
    init {
        viewModelScope.launch {
            _isLoading.value = true
            allNotes
                .catch { e ->
                    _error.value = e.message ?: "Error loading notes"
                    _isLoading.value = false
                }
                .collect { notes ->
                    _filteredNotes.value = notes
                    _isLoading.value = false
                }
        }
    }
}