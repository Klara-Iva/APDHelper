package com.example.apdhelper.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeViewModel : ViewModel() {

    var greeting by mutableStateOf("Hello")
        private set

    var userName by mutableStateOf<String?>(null)
        private set

    var testCount by mutableStateOf(0)
        private set

    var noteCount by mutableStateOf(0)
        private set

    var streak by mutableStateOf(0)
        private set

    var quote by mutableStateOf("")
        private set

    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val firestore = Firebase.firestore

    suspend fun loadUserInfo() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        greeting = when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..21 -> "Good evening"
            else -> "Hello"
        }

        if (uid != null) {
            val doc =
                FirebaseFirestore.getInstance().collection("users").document(uid).get().await()
            userName = doc.getString("name")
        }
    }

    suspend fun loadProgressStats() {
        if (uid == null) return

        val tests = firestore.collection("users").document(uid).collection("tests")
            .document("PanicDisorderTests").collection("entries").get().await()
        testCount = tests.size()

        val notes = firestore.collection("users").document(uid).collection("notes").get().await()
        noteCount = notes.size()

        val visits = firestore.collection("users").document(uid).collection("visits").get().await()

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dates = visits.documents.mapNotNull { doc ->
            try {
                formatter.parse(doc.id)
            } catch (_: Exception) {
                null
            }
        }.sortedDescending()

        var count = 0
        val calendar = Calendar.getInstance()

        for (date in dates) {
            val todayStr = formatter.format(calendar.time)
            val visitStr = formatter.format(date)

            if (visitStr == todayStr) {
                count++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
            } else break
        }

        streak = count
    }

    suspend fun loadQuote() {
        quote = fetchRandomQuote()
    }

    private suspend fun fetchRandomQuote(): String {
        return withContext(Dispatchers.IO) {
            try {
                val connection =
                    URL("https://zenquotes.io/api/random").openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val response = connection.inputStream.bufferedReader().readText()
                val jsonArray = JSONArray(response)
                val quoteObj = jsonArray.getJSONObject(0)
                val quote = quoteObj.getString("q")
                val author = quoteObj.getString("a")
                "$quote — $author"
            } catch (e: Exception) {
                "Breathe deeply. You got this."
            }
        }
    }
}
