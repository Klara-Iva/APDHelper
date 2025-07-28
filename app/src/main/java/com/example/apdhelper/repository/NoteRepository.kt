package com.example.apdhelper.repository

import com.example.apdhelper.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NotesRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun addNote(note: Note) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("notes").add(note).await()
    }

    suspend fun deleteNote(noteId: String) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("notes").document(noteId).delete().await()
    }

    fun getNotesFlow() = callbackFlow<List<Note>> {
        val uid = auth.currentUser?.uid ?: return@callbackFlow
        val listener = db.collection("users").document(uid).collection("notes")
            .addSnapshotListener { snapshot, error ->
                if (error == null && snapshot != null) {
                    val notes = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Note::class.java)?.copy(id = doc.id)
                    }
                    trySend(notes)
                }
            }
        awaitClose { listener.remove() }
    }
}
