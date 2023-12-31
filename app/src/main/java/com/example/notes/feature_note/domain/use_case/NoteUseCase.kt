package com.example.notes.feature_note.domain.use_case

data class NoteUseCase(
    val getNotes: GetNotes,
    val getNote: GetNote,
    val deleteNote: DeleteNote,
    val addEditNote: AddEditNote
)