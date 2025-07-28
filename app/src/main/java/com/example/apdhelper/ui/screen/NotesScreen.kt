package com.example.apdhelper.ui.screen.notes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apdhelper.ui.screen.notes.components.NoteCard
import com.example.apdhelper.ui.theme.MutedText
import com.example.apdhelper.ui.theme.Primary
import com.example.apdhelper.ui.theme.TextPrimary
import com.example.apdhelper.viewmodel.NotesViewModel

@Composable
fun NotesScreen(navController: NavController) {
    val viewModel: NotesViewModel = viewModel()
    val context = LocalContext.current
    val notes by viewModel.filteredNotes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

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

        SearchBar(
            text = viewModel.searchQuery.collectAsState().value,
            onTextChange = viewModel::onSearchQueryChanged
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "HINT: Long press on a note deletes it",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(notes) { note ->
                NoteCard(note = note, onDelete = {
                    viewModel.deleteNote(it)
                    Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                })
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SearchBar(text: String, onTextChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .border(1.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart
    ) {
        if (text.isEmpty()) {
            Text("Search your notes...", fontSize = 14.sp, color = MutedText)
        }
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            singleLine = true,
            textStyle = TextStyle(color = TextPrimary, fontSize = 14.sp),
            cursorBrush = SolidColor(Primary),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
