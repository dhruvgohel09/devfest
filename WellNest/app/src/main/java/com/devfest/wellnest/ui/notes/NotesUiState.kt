package com.devfest.wellnest.ui.notes

import com.devfest.wellnest.data.local.entity.NoteEntity

data class NotesUiState(
    val isLoading: Boolean = false,
    val notes: List<NoteEntity> = emptyList(),
    val searchQuery: String = "",
    val showAddDialog: Boolean = false,
    val editingNote: NoteEntity? = null,
    val error: String? = null
)
