package com.example.mykisah.di

import android.app.Application
import androidx.room.Room
import com.example.mykisah.data.local.dao.NoteDao
import com.example.mykisah.data.local.db.NoteDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {
    @Provides
    @Singleton
    fun ProvideAppDatabase(application: Application): NoteDb {
        return Room.databaseBuilder(
            application.applicationContext,
            NoteDb::class.java,
            "note_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(db: NoteDb): NoteDao {
        return db.noteDao()
    }
}