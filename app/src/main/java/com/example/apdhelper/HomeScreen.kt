package com.example.apdhelper

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import com.example.apdhelper.R
import com.example.apdhelper.ui.theme.Background
import com.example.apdhelper.ui.theme.IconTintPrimary
import com.example.apdhelper.ui.theme.Primary
import com.example.apdhelper.ui.theme.Secondary
import com.example.apdhelper.ui.theme.TextPrimary
import kotlin.system.exitProcess
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.LaunchedEffect
import java.util.Calendar

@Composable
fun HomeScreen(navController: NavController) {
    BackHandler {
        exitProcess(0)
    }

    var screenState by remember { mutableStateOf("home") }
    var assessmentResults by remember { mutableStateOf<Map<String, Int>?>(null) }

    val quickStats = listOf(
        Triple("Tests Completed", "12", "🧠"),
        Triple("Notes Created", "28", "📝"),
        Triple("Days Streak", "5", "🔥")
    )

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
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .get()
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
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                    text = "You're doing amazing! How are you feeling tonight? ✨",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Quick Actions 🎯",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Background
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = Secondary,
                                shape = RoundedCornerShape(16.dp)
                            )

                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🧠", fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        "Take Assessment 📊",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Primary
                                    )
                                    Text(
                                        "Check your current anxiety levels and track your progress",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { screenState = "questionnaire" },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Start Assessment 🚀", color = Background)
                            }
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Background
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = Secondary,
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            color = Secondary,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Secondary,
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_description_24),
                                        contentDescription = "Heart Outline",
                                        tint = TextPrimary,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        "Log Experience 📝",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Primary
                                    )
                                    Text(
                                        "Record your feelings, thoughts, or any experiences",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = { },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("Create Note 💭", color = Primary)
                            }
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Your Amazing Progress 📈",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    quickStats.forEach { (label, value, icon) ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(130.dp)
                                .border(
                                    width = 1.dp,
                                    color = Secondary,
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Background)
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
                                Text(label, fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                            }
                        }

                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Background),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Secondary,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "\uD83C\uDF19 Tonight's Check-in 🌟",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("How would you rate your overall mood tonight? (1–10) ✨", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        (1..10).forEach { num ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { }
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = num.toString(), fontSize = 12.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Remember: every feeling is valid, and you're brave for being here 💜",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
