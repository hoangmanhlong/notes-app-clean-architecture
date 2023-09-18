package com.example.notes.feature_note.data.repository

import com.example.notes.feature_note.data.local.NoteDatabase
import com.example.notes.feature_note.domain.model.Note
import com.example.notes.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val database: NoteDatabase
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> = database.dao.getNotes()

    override suspend fun getNote(id: Int): Note? = database.dao.getNote(id)

    override suspend fun insert(note: Note) = database.dao.insert(note)

    override suspend fun delete(note: Note) = database.dao.delete(note)
}