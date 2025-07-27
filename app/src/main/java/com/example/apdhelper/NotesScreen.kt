package com.example.apdhelper

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.apdhelper.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val date: String = "",
    val moodLevel: Int = 0,
    val anxietyLevel: Int = 0,
    val triggers: List<String> = emptyList(),
    val tags: List<String> = emptyList()
)


@Composable
fun NotesScreen(navController: NavController) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseFirestore.getInstance()
    var notes by remember { mutableStateOf<List<Note>>(emptyList()) }
    var searchText by remember { mutableStateOf("") }

    val filteredNotes = notes.filter {
        it.title.contains(searchText, ignoreCase = true) || it.content.contains(
            searchText,
            ignoreCase = true
        )
    }

    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("users").document(uid).collection("notes")
                .addSnapshotListener { snapshot, error ->
                    if (error != null || snapshot == null) return@addSnapshotListener
                    notes = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Note::class.java)?.copy(id = doc.id)
                    }.sortedByDescending {
                        SimpleDateFormat(
                            "dd. MM. yyyy. HH:mm:ss", Locale.getDefault()
                        ).parse(it.date)?.time ?: 0L
                    }
                }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, bottom = 0.dp, start = 24.dp, end = 24.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Notes Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("My Notes", fontSize = 25.sp, color = MaterialTheme.colorScheme.primary)
            }
            Button(
                onClick = { navController.navigate("add_note") },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Icon",
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("New Note", fontSize = 15.sp, color = MaterialTheme.colorScheme.surface)
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .border(1.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart
        ) {
            if (searchText.isEmpty()) {
                Text("Search your notes...", fontSize = 14.sp, color = MutedText)
            }
            BasicTextField(
                value = searchText,
                onValueChange = { searchText = it },
                singleLine = true,
                textStyle = TextStyle(color = TextPrimary, fontSize = 14.sp),
                cursorBrush = SolidColor(Primary),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard("Total Notes", notes.size.toString(), TextPrimary)

            val thisWeekCount = notes.count {
                try {
                    val formatter = SimpleDateFormat("dd. MM. yyyy. HH:mm:ss", Locale.getDefault())
                    val noteDate = formatter.parse(it.date)
                    noteDate != null && Date().time - noteDate.time <= 7 * 24 * 60 * 60 * 1000
                } catch (e: Exception) {
                    false
                }
            }


            StatCard("This Week", thisWeekCount.toString(), TextPrimary)

            val avgMood =
                notes.map { it.moodLevel }.takeIf { it.isNotEmpty() }?.average()?.toInt() ?: 0
            StatCard("Avg Mood", "$avgMood", getMoodColor(avgMood))

        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Recent Notes", fontSize = 16.sp, color = TextPrimary)

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxSize()
        ) {
            items(filteredNotes) { note ->
                NoteCard(note)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteCard(note: Note) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    val pressed = remember { mutableStateOf(false) }

    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier
        .fillMaxWidth()
        .then(
            if (!pressed.value) Modifier.background(
                brush = themedGradientBrush(), shape = RoundedCornerShape(16.dp)
            )
            else Modifier.background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
        )

        .pointerInput(note.id) {
            detectTapGestures(onLongPress = {
                pressed.value = true
                if (uid != null && note.id.isNotBlank()) {
                    db
                        .collection("users")
                        .document(uid)
                        .collection("notes")
                        .document(note.id)
                        .delete()
                        .addOnSuccessListener {
                            Toast
                                .makeText(context, "Note deleted", Toast.LENGTH_SHORT)
                                .show()
                            pressed.value = false
                        }
                        .addOnFailureListener {
                            Toast
                                .makeText(context, "Failed to delete", Toast.LENGTH_SHORT)
                                .show()
                            pressed.value = false
                        }
                } else {
                    pressed.value = false
                }
            }, onPress = {
                pressed.value = true
                tryAwaitRelease()
                pressed.value = false
            })
        }
        .border(
            1.dp, MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), RoundedCornerShape(16.dp)
        ), colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Mood Icon",
                    tint = getMoodColor(note.moodLevel),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("${note.moodLevel}/10", fontSize = 13.sp, color = getMoodColor(note.moodLevel))

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "🧠 ${note.anxietyLevel}/10",
                    fontSize = 13.sp,
                    color = getAnxietyColor(note.anxietyLevel)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = note.content, fontSize = 14.sp, color = TextPrimary
            )

            if (note.triggers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("⚡ Triggers:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    note.triggers.forEach {
                        TriggerChip(it)
                    }
                }
            }

            if (note.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("🏷 Tags:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    note.tags.forEach {
                        TagChip(it)
                    }
                }
            }
        }
    }
}


fun getMoodColor(level: Int): Color {
    return when (level) {
        in 8..10 -> Color(0xFF37B36A)
        in 5..7 -> Color(0xFFFFA500)
        else -> Color(0xFFFF4D4D)
    }
}

fun getAnxietyColor(level: Int): Color {
    return when (level) {
        in 0..3 -> Color(0xFF37B36A)
        in 4..6 -> Color(0xFFFFA500)
        else -> Color(0xFFFF4D4D)
    }
}

@Composable
fun TriggerChip(text: String) {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) {
        Surface.copy(alpha = 0.1f)
    } else {
        Color(0xFFFFF3CC)
    }

    val borderColor = if (isDark) {
        Color(0xFFFFD700).copy(alpha = 0.4f)
    } else {
        Color(0xFFFFD700).copy(alpha = 0.6f)
    }

    val textColor = if (isDark) {
        Color(0xFFFFD700)
    } else {
        Color(0xFFB88900)
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = textColor
        )
    }
}


@Composable
fun TagChip(text: String) {
    Box(
        modifier = Modifier
            .background(
                Color(0xFFB3A9D1).copy(alpha = 0.1f), RoundedCornerShape(20.dp)
            )
            .border(1.dp, Color(0xFFB3A9D1).copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text, fontSize = 12.sp, color = Color(0xFFB3A9D1)
        )
    }
}


@Composable
fun StatCard(title: String, number: String, numberColor: Color) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .height(84.dp)
            .border(1.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number, fontSize = 20.sp, fontWeight = FontWeight.Medium, color = numberColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}
