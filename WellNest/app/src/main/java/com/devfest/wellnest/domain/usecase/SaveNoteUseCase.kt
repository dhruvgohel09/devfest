package com.devfest.wellnest.domain.usecase

import com.devfest.wellnest.data.local.entity.NoteEntity
import com.devfest.wellnest.data.repository.NoteRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: NoteEntity): Long = noteRepository.insertNote(note)

    suspend fun update(note: NoteEntity) = noteRepository.updateNote(note)

    suspend fun delete(note: NoteEntity) = noteRepository.deleteNote(note)
}
