package com.example.notes.feature_note.domain.use_case

import com.example.notes.feature_note.domain.model.Note
import com.example.notes.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

import javax.inject.Inject

class GetNotes @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getNotes().catch {
            if (it is IOException) {
                it.printStackTrace()
            } else {
                throw it
            }
        }
    }
}