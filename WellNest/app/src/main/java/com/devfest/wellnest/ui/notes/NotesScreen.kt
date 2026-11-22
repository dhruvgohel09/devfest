package com.devfest.wellnest.ui.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devfest.wellnest.data.local.entity.NoteEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    viewModel: NotesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Notes", fontWeight = FontWeight.Bold) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::showAddDialog) {
                Icon(Icons.Filled.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Search bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search notes...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true
            )

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.notes.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No notes yet. Create your first note!",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }
                }
                items(uiState.notes) { note ->
                    NoteCard(
                        note = note,
                        onEdit = { viewModel.editNote(note) },
                        onDelete = { viewModel.deleteNote(note) },
                        onTogglePin = { viewModel.togglePin(note) }
                    )
                }
            }
        }
    }

    if (uiState.showAddDialog) {
        NoteDialog(
            existingNote = uiState.editingNote,
            onDismiss = viewModel::hideAddDialog,
            onSave = viewModel::saveNote
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteCard(
    note: NoteEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onTogglePin: () -> Unit
) {
    val dateStr = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(note.updatedAt))
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onEdit
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Text(note.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f))
                Row {
                    IconButton(onClick = onTogglePin) {
                        Icon(Icons.Filled.PushPin, contentDescription = "Pin",
                            tint = if (note.isPinned) MaterialTheme.colorScheme.primary
                                   else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            if (note.body.isNotBlank()) {
                Text(note.body, style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                if (note.tags.isNotBlank()) {
                    Text(note.tags.replace(",", " · "), style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary)
                }
                Text(dateStr, style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun NoteDialog(
    existingNote: NoteEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(existingNote?.title ?: "") }
    var body by remember { mutableStateOf(existingNote?.body ?: "") }
    var tags by remember { mutableStateOf(existingNote?.tags ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existingNote == null) "New Note" else "Edit Note") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it },
                    label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = body, onValueChange = { body = it },
                    label = { Text("Content") }, modifier = Modifier.fillMaxWidth(),
                    minLines = 3, maxLines = 6)
                OutlinedTextField(value = tags, onValueChange = { tags = it },
                    label = { Text("Tags (comma separated)") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(title, body, tags) }, enabled = title.isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
