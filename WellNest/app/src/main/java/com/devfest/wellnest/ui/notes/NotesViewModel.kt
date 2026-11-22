package com.devfest.wellnest.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devfest.wellnest.data.local.entity.NoteEntity
import com.devfest.wellnest.domain.usecase.SaveNoteUseCase
import com.devfest.wellnest.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val saveNoteUseCase: SaveNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _searchQuery.flatMapLatest { query ->
                if (query.isBlank()) noteRepository.getAllNotes()
                else noteRepository.searchNotes(query)
            }.collect { notes ->
                _uiState.update { it.copy(notes = notes, isLoading = false) }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun showAddDialog() = _uiState.update { it.copy(showAddDialog = true, editingNote = null) }
    fun hideAddDialog() = _uiState.update { it.copy(showAddDialog = false, editingNote = null) }

    fun editNote(note: NoteEntity) = _uiState.update { it.copy(showAddDialog = true, editingNote = note) }

    fun saveNote(title: String, body: String, tags: String) {
        viewModelScope.launch {
            val existing = _uiState.value.editingNote
            if (existing != null) {
                saveNoteUseCase.update(existing.copy(title = title, body = body, tags = tags,
                    updatedAt = System.currentTimeMillis()))
            } else {
                saveNoteUseCase(NoteEntity(title = title, body = body, tags = tags))
            }
            hideAddDialog()
        }
    }

    fun togglePin(note: NoteEntity) {
        viewModelScope.launch {
            noteRepository.updateNote(note.copy(isPinned = !note.isPinned))
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch { saveNoteUseCase.delete(note) }
    }
}
