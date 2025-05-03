package com.example.mykisah.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mykisah.data.local.dao.NoteDao
import com.example.mykisah.data.local.models.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDb : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object
    {
        @Volatile
        private var INSTANCE: NoteDb? = null

        fun getDatabase(context: Context): NoteDb {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    NoteDb::class.java,
                    "note_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}