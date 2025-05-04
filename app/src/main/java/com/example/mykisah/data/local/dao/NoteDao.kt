package com.example.mykisah.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import  androidx.room.Delete
import  androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mykisah.data.local.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: String): Note?

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes WHERE category = :category ORDER BY title ASC")
    fun getNotesByCategory(category: String): Flow<List<Note>>

    @Query("SELECT DISTINCT category FROM notes")
    fun getAllCategories(): Flow<List<String>>


}
