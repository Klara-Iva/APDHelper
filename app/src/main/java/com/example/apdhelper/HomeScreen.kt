package com.example.apdhelper

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.system.exitProcess
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.apdhelper.bottomnavigationbar.BottomNavigationItems
import java.util.Calendar
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import com.example.apdhelper.ui.theme.Secondary
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun HomeScreen(navController: NavController) {
    BackHandler {
        exitProcess(0)
    }

    var screenState by remember { mutableStateOf("home") }
    var assessmentResults by remember { mutableStateOf<Map<String, Int>?>(null) }

    if (screenState == "questionnaire") {
        // TODO: Navigate to questionnaire screen
        return
    }

    if (screenState == "results" && assessmentResults != null) {
        // TODO: Navigate to results screen
        return
    }

    var userName by remember { mutableStateOf<String?>(null) }
    var greeting by remember { mutableStateOf("Hello") }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    userName = document.getString("name")
                }
        }
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        greeting = when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..21 -> "Good evening"
            else -> "Hello"
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val scrollState = rememberScrollState()

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$greeting, ${userName ?: "..."}! 🌙",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "You're doing amazing! How are you feeling today? ✨",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Quick Actions 🎯", fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = themedGradientBrush(), shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(16.dp)
                                        ), contentAlignment = Alignment.Center
                                ) {
                                    Text("🧠", fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        "Take Assessment 📊",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(
                                        "Check if you are dealing with panic disorder or test your anxiety",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    navController.navigate(BottomNavigationItems.Test.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }, modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Start Assessment 🚀",
                                    color = MaterialTheme.colorScheme.background
                                )
                            }

                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background

                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = RoundedCornerShape(16.dp)
                                        ), contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_description_24),
                                        contentDescription = "Heart Outline",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        "Log Experience 📝",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(
                                        "Record your feelings, thoughts, or any experiences",
                                        fontSize = 13.sp,
                                        lineHeight = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = {
                                    navController.navigate(BottomNavigationItems.Notes.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("Create Note 💭", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }

            ProgressSection()

            MoodRatingSection(scrollState = scrollState)


        }
    }
}


@Composable
fun ProgressSection() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = Firebase.firestore

    var testCount by remember { mutableStateOf(0) }
    var noteCount by remember { mutableStateOf(0) }
    var streak by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            firestore.collection("users").document(uid).collection("tests")
                .document("PanicDisorderTests").collection("entries").get()
                .addOnSuccessListener { result ->
                    testCount = result.size()
                }

            firestore.collection("users").document(uid).collection("notes").get()
                .addOnSuccessListener { result ->
                    noteCount = result.size()
                }

            firestore.collection("users").document(uid).collection("visits").get()
                .addOnSuccessListener { result ->
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val dates = result.documents.mapNotNull { doc ->
                        try {
                            formatter.parse(doc.id)
                        } catch (e: Exception) {
                            null
                        }
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
                        } else {
                            break
                        }
                    }

                    streak = count
                }
        }
    }

    val quickStats = listOf(
        Triple("Tests Completed", testCount.toString(), "🧠"),
        Triple("Notes Created", noteCount.toString(), "📝"),
        Triple("Days Streak", streak.toString(), "🔥")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Your Amazing Progress 📈", fontSize = 16.sp, fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()
        ) {
            quickStats.forEach { (label, value, icon) ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(16.dp)
                        ), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.5f
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(icon, fontSize = 20.sp)
                        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            label,
                            lineHeight = 13.sp,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun MoodRatingSection(scrollState: ScrollState) {
    var selectedRating by rememberSaveable { mutableStateOf<Int?>(null) }
    var moodMessage by rememberSaveable { mutableStateOf("") }
    var quote by rememberSaveable { mutableStateOf("") }


    LaunchedEffect(quote) {
        if (quote.isNotBlank()) {
            delay(100)
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }


    LaunchedEffect(selectedRating) {
        if (selectedRating != null && quote.isBlank()) {
            quote = fetchRandomQuote()
        }
    }





    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = themedGradientBrush(), shape = RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "\uD83C\uDF19 Today's Check-in 🌟",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("How would you rate your overall mood today? (1–10) ✨", fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                (1..10).forEach { num ->
                    Box(modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = if (selectedRating == num) MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.2f
                            )
                            else Color.Transparent, shape = CircleShape
                        )
                        .clickable {
                            selectedRating = num
                            moodMessage = when (num) {
                                in 1..3 -> "So sorry to hear that 💙"
                                in 4..7 -> "Thanks for sharing 💫"
                                else -> "That's awesome! 🌈"
                            }

                            quote = ""
                        }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ), contentAlignment = Alignment.Center) {
                        Text(text = num.toString(), fontSize = 12.sp)
                    }
                }
            }

            if (selectedRating != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = moodMessage,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (quote.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = "Quote Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Here's something for you:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "“$quote”",
                        fontStyle = FontStyle.Italic,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    )
                }
            }

        }
    }
}


suspend fun fetchRandomQuote(): String {
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
