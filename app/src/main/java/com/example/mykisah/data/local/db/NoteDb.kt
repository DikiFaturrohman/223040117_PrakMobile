package com.example.mykisah.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mykisah.data.local.dao.NoteDao
import com.example.mykisah.data.local.models.Note

@Database(entities = [Note::class], version = 2)
abstract class NoteDb : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDb? = null

        fun getDatabase(context: Context): NoteDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDb::class.java,
                    "notes_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}