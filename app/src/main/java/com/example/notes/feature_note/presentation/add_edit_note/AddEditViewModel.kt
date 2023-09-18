package com.example.notes.feature_note.presentation.add_edit_note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.feature_note.domain.model.Note
import com.example.notes.feature_note.domain.use_case.NoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val noteUseCase: NoteUseCase
) : ViewModel() {

    companion object {
        var selectedNote: Note? = null
    }

    private val _note = MutableLiveData<Note?>()
    val note: LiveData<Note?>
        get() = _note

    fun getNote(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _note.postValue(noteUseCase.getNote(id))
            } catch (exception: Exception) {
                exception.stackTrace
                _note.value = null
            }
        }
    }

    fun update(
        id: Int,
        title: String,
        content: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val note = Note(id = id, title = title, content = content)
                noteUseCase.addEditNote(note)
            } catch (exception: Exception) {
                exception.stackTrace
            }
        }
    }

    fun add(
        title: String,
        content: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val note = Note(title = title, content = content)
                noteUseCase.addEditNote(note)
            } catch (exception: Exception) {
                exception.stackTrace
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                noteUseCase.deleteNote(note)
            } catch (exception: Exception) {
                exception.stackTrace
            }
        }
    }
}