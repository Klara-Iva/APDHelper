package com.example.apdhelper

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apdhelper.ui.theme.*

@Composable
fun NotesScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header + New Note button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "My Notes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Primary
                )
                Text(
                    text = "Track your mental health journey",
                    fontSize = 14.sp,
                    color = MutedText,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            IconButton(
                onClick = { /* TODO: New Note action */ },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Primary)
                    .size(width = 100.dp, height = 36.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = TextWhite
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "New Note",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Search bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Secondary)
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (searchText.isEmpty()) {
                Text(
                    text = "Search notes...",
                    fontSize = 14.sp,
                    color = MutedText
                )
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

        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard(title = "Total Notes", number = "28", numberColor = TextPrimary)
            StatCard(title = "Good Days", number = "15", numberColor = Color(0xFF37B36A))
            StatCard(title = "This Week", number = "5", numberColor = Color(0xFFED6836))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recent Notes header
        Text(
            text = "Recent Notes",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Notes List - hardcoded sample items
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NoteCard(
                title = "Panic Attack at Work",
                description = "Had a panic attack during the morning meeting. Heart racing, difficulty breathing...",
                labelText = "Panic Attack",
                labelColor = Color(0xFFFF4D4D),
                severity = "7/10",
                dateTime = "2024-01-18 at 10:30 AM",
                tags = listOf("work", "meeting", "breathing")
            )
            NoteCard(
                title = "Good Day - Feeling Calm",
                description = "Practiced breathing exercises this morning. Felt much more centered throughout the day...",
                labelText = "Positive",
                labelColor = Color(0xFF37B36A),
                severity = "3/10",
                dateTime = "2024-01-17 at 8:00 PM",
                tags = listOf("breathing", "calm", "exercise")
            )
            NoteCard(
                title = "Anxiety Before Presentation",
                description = "Nervous about tomorrow's presentation. Using grounding techniques to manage...",
                labelText = "Anxiety",
                labelColor = Color(0xFFFFA500),
                severity = "5/10",
                dateTime = "2024-01-16 at 9:15 PM",
                tags = listOf("presentation", "work", "grounding")
            )
        }
    }
}

@Composable
fun StatCard(title: String, number: String, numberColor: androidx.compose.ui.graphics.Color) {
    Column(
        modifier = Modifier
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = numberColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            color = MutedText
        )
    }
}

@Composable
fun NoteCard(
    title: String,
    description: String,
    labelText: String,
    labelColor: androidx.compose.ui.graphics.Color,
    severity: String,
    dateTime: String,
    tags: List<String>
) {
    Surface(
        color = Surface,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Label(text = labelText, backgroundColor = labelColor)
                Text(
                    text = "Severity: $severity",
                    fontSize = 12.sp,
                    color = labelColor,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = dateTime,
                fontSize = 12.sp,
                color = MutedText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    TagChip(tag)
                }
            }
        }
    }
}

@Composable
fun Label(text: String, backgroundColor: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = TextWhite,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TagChip(text: String) {
    Box(
        modifier = Modifier
            .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = MutedText
        )
    }
}
