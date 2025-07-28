package com.example.apdhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ProfileState(
    val name: String? = null,
    val email: String? = null,
    val memberSince: String = "Unknown",
    val testCount: Int = 0,
    val noteCount: Int = 0,
    val streak: Int = 0,
    val loading: Boolean = true
)

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        val uid = auth.currentUser?.uid ?: return

        val memberSince = auth.currentUser?.metadata?.creationTimestamp?.let {
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(it))
        } ?: "Unknown"

        _state.value = _state.value.copy(memberSince = memberSince)

        viewModelScope.launch {
            db.collection("users").document(uid).get().addOnSuccessListener { doc ->
                    _state.value = _state.value.copy(
                        name = doc.getString("name"),
                        email = doc.getString("email"),
                        loading = false
                    )
                }

            db.collection("users").document(uid).collection("tests").document("PanicDisorderTests")
                .collection("entries").get().addOnSuccessListener {
                    _state.value = _state.value.copy(testCount = it.size())
                }

            db.collection("users").document(uid).collection("notes").get().addOnSuccessListener {
                    _state.value = _state.value.copy(noteCount = it.size())
                }

            db.collection("users").document(uid).collection("visits").get()
                .addOnSuccessListener { result ->
                    val dates = result.documents.mapNotNull { it.id }.mapNotNull {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
                    }.sortedDescending()

                    var count = 0
                    val calendar = Calendar.getInstance()

                    for (date in dates) {
                        val todayStr = SimpleDateFormat(
                            "yyyy-MM-dd", Locale.getDefault()
                        ).format(calendar.time)
                        val visitStr =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                        if (visitStr == todayStr) {
                            count++
                            calendar.add(Calendar.DAY_OF_YEAR, -1)
                        } else break
                    }

                    _state.value = _state.value.copy(streak = count)
                }
        }
    }
}
