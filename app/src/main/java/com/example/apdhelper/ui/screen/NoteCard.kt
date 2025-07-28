package com.example.apdhelper.ui.screen.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apdhelper.model.Note
import com.example.apdhelper.ui.theme.TextPrimary
import com.example.apdhelper.ui.theme.themedGradientBrush

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteCard(note: Note, onDelete: (String) -> Unit) {
    var pressed by remember { mutableStateOf(false) }

    Card(shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(note.id) {
                detectTapGestures(onLongPress = {
                    pressed = true
                    if (note.id.isNotBlank()) onDelete(note.id)
                    pressed = false
                }, onPress = {
                    pressed = true
                    tryAwaitRelease()
                    pressed = false
                })
            }
            .background(
                if (pressed) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                else Color.Transparent, shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(themedGradientBrush(), shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = note.title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
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

            Text(note.content, fontSize = 14.sp, color = TextPrimary)

            if (note.triggers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("⚡ Triggers:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(
                    modifier = Modifier
                        .height(6.dp)
                        .fillMaxWidth()
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    note.triggers.forEach { TriggerChip(it) }
                }
            }

            if (note.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("🏷 Tags:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    note.tags.forEach { TagChip(it) }
                }
            }
        }
    }
}

fun getMoodColor(level: Int): Color = when (level) {
    in 8..10 -> Color(0xFF37B36A)
    in 5..7 -> Color(0xFFFFA500)
    else -> Color(0xFFFF4D4D)
}

fun getAnxietyColor(level: Int): Color = when (level) {
    in 0..3 -> Color(0xFF37B36A)
    in 4..6 -> Color(0xFFFFA500)
    else -> Color(0xFFFF4D4D)
}

@Composable
fun TriggerChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFF3CC), RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFFB88900)
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
        Text(text = text, fontSize = 12.sp, color = Color(0xFFB3A9D1))
    }
}
