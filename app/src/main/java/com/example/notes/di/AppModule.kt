package com.example.notes.di

import android.app.Application
import androidx.room.Room
import com.example.notes.feature_note.data.local.NoteDatabase
import com.example.notes.feature_note.data.repository.NoteRepositoryImpl
import com.example.notes.feature_note.domain.repository.NoteRepository
import com.example.notes.feature_note.domain.use_case.AddEditNote
import com.example.notes.feature_note.domain.use_case.DeleteNote
import com.example.notes.feature_note.domain.use_case.GetNote
import com.example.notes.feature_note.domain.use_case.GetNotes
import com.example.notes.feature_note.domain.use_case.NoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(
        application: Application
    ): NoteDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(
        database: NoteDatabase
    ): NoteRepository {
        return NoteRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideNoteUseCase(
        repository: NoteRepository
    ): NoteUseCase {
        return NoteUseCase(
            getNotes = GetNotes(repository),
            getNote = GetNote(repository),
            addEditNote = AddEditNote(repository),
            deleteNote = DeleteNote(repository)
        )
    }
}