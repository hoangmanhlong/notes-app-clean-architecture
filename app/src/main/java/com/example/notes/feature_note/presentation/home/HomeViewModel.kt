package com.example.notes.feature_note.presentation.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.feature_note.data.setting.LayoutSettingDataStore
import com.example.notes.feature_note.domain.model.Note
import com.example.notes.feature_note.domain.use_case.NoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    noteUseCase: NoteUseCase,
    application: Application
) : ViewModel() {


    private val layoutSettingDataStore = LayoutSettingDataStore(application)

    var isLinearLayoutManager = true

    fun saveLayout(
        context: Context
    ) {
        viewModelScope.launch {
            try {
                layoutSettingDataStore.saveLayout(isLinearLayoutManager, context)
            } catch (exception: Exception) {
                exception.stackTrace
            }
        }
    }

    val layoutState: LiveData<Boolean> = layoutSettingDataStore.preferencesFlow.asLiveData()

    val notes: LiveData<List<Note>> = noteUseCase.getNotes().asLiveData()
}