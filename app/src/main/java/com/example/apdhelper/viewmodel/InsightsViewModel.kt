package com.example.apdhelper.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.apdhelper.model.RecommendationItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class InsightsViewModel : ViewModel() {

    var recommendations by mutableStateOf<List<RecommendationItem>>(emptyList())
        private set

    var loading by mutableStateOf(true)
        private set

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun loadRecommendations() {
        val uid = auth.currentUser?.uid ?: return

        val snapshot = db.collection("users").document(uid).collection("insights")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING).get()
            .await()

        recommendations = snapshot.documents.mapNotNull { doc ->
            val text = doc.getString("recommendation")
            val timestamp = doc.getTimestamp("timestamp")
            if (text != null && timestamp != null) {
                RecommendationItem(text, timestamp)
            } else null
        }

        loading = false
    }
}
