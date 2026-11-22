package com.devfest.wellnest.data.repository

import com.devfest.wellnest.data.local.dao.NoteDao
import com.devfest.wellnest.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()
    fun getPinnedNotes(): Flow<List<NoteEntity>> = noteDao.getPinnedNotes()
    fun searchNotes(query: String): Flow<List<NoteEntity>> = noteDao.searchNotes(query)
    suspend fun insertNote(note: NoteEntity) = noteDao.insertNote(note)
    suspend fun updateNote(note: NoteEntity) = noteDao.updateNote(note)
    suspend fun deleteNote(note: NoteEntity) = noteDao.deleteNote(note)
}
