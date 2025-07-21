package com.example.apdhelper

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ProfileScreen(
    navController: NavController,
    testsCompleted: Int = 12,
    notesWritten: Int = 28,
    daysActive: Int = 45,
    onSettingsClick: () -> Unit = {},
    onHelpClick: () -> Unit = {}
) {
    // Boje, možeš ih staviti u svoj theme file i importati
    val bgColor = Color(0xFF1C1732)
    val cardColor = Color(0xFF2B2547)
    val primaryColor = Color(0xFF9887F3) // ljubičasta za istaknuto
    val secondaryText = Color(0xFFC7C0D4)
    val mutedText = Color(0xFF857FB5)
    val errorRed = Color(0xFFB35353)

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var userName by remember { mutableStateOf<String?>(null) }
    var userEmail by remember { mutableStateOf<String?>(null) }
    var memberSince by remember { mutableStateOf("Unknown") }
    var loading by remember { mutableStateOf(true) }

    val uid = auth.currentUser?.uid

    LaunchedEffect(uid) {
        if (uid != null) {
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
                        userName = doc.getString("name")
                        userEmail = doc.getString("email")
                        memberSince = doc.getString("memberSince") ?: "Unknown"
                    } else {
                        Toast.makeText(context, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                    loading = false
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error loading user data: ${e.message}", Toast.LENGTH_LONG).show()
                    loading = false
                }
        } else {
            navController.navigate("start") {
                popUpTo("profile") { inclusive = true }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = bgColor
    ) {
        if (loading) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // PROFILE HEADER CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(primaryColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userName?.split(" ")?.map { it.first() }?.joinToString("") ?: "?",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(primaryColor.copy(alpha = 0.7f))
                                    .border(1.dp, Color.White, CircleShape)
                                    .align(Alignment.BottomEnd)
                                    .clickable { /* Edit profile click logic */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = userName ?: "Unknown",
                            color = secondaryText,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = "Email Icon",
                                tint = mutedText,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = userEmail ?: "Unknown",
                                color = secondaryText,
                                fontSize = 15.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarToday,
                                contentDescription = "Member since Icon",
                                tint = mutedText,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Member since $memberSince",
                                color = secondaryText,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // YOUR STATISTICS CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Your Statistics",
                            color = mutedText,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatisticItem(
                                count = testsCompleted,
                                label = "Tests Completed",
                                isHighlighted = true,
                                primaryColor = primaryColor,
                                secondaryColor = cardColor,
                                textColor = Color.White
                            )
                            StatisticItem(
                                count = notesWritten,
                                label = "Notes Written",
                                isHighlighted = false,
                                primaryColor = primaryColor,
                                secondaryColor = cardColor,
                                textColor = mutedText
                            )
                            StatisticItem(
                                count = daysActive,
                                label = "Days Active",
                                isHighlighted = false,
                                primaryColor = primaryColor,
                                secondaryColor = cardColor,
                                textColor = mutedText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ACHIEVEMENTS CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.EmojiEvents,
                                contentDescription = "Achievements Icon",
                                tint = mutedText,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Achievements",
                                color = mutedText,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        AchievementItem(
                            icon = "🏆",
                            title = "7-Day Streak",
                            description = "Completed daily check-ins",
                            primaryColor = primaryColor,
                            secondaryColor = mutedText
                        )
                        AchievementItem(
                            icon = "🎯",
                            title = "First Assessment",
                            description = "Completed your first anxiety test",
                            primaryColor = primaryColor,
                            secondaryColor = mutedText
                        )
                        AchievementItem(
                            icon = "✍️",
                            title = "Mindful Writer",
                            description = "Created 10+ notes",
                            primaryColor = primaryColor,
                            secondaryColor = mutedText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ACCOUNT CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Account",
                            color = mutedText,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        ProfileMenuItem(
                            text = "Settings & Privacy",
                            icon = Icons.Outlined.Settings,
                            onClick = onSettingsClick,
                            backgroundColor = cardColor,
                            contentColor = mutedText
                        )
                        ProfileMenuItem(
                            text = "Help & Support",
                            icon = Icons.Outlined.Help,
                            onClick = onHelpClick,
                            backgroundColor = cardColor,
                            contentColor = mutedText
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileMenuItem(
                            text = "Log Out",
                            icon = Icons.Outlined.Logout,
                            onClick = {
                                auth.signOut()
                                navController.navigate("start") {
                                    popUpTo("profile") { inclusive = true }
                                }
                            },
                            backgroundColor = errorRed,
                            contentColor = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatisticItem(
    count: Int,
    label: String,
    isHighlighted: Boolean,
    primaryColor: Color,
    secondaryColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
           .height(84.dp)
            .background(
                color = if (isHighlighted) primaryColor else secondaryColor,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$count",
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AchievementItem(
    icon: String,
    title: String,
    description: String,
    primaryColor: Color,
    secondaryColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = icon, fontSize = 26.sp, modifier = Modifier.padding(end = 14.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    color = primaryColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .background(primaryColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = "Earned", fontSize = 12.sp, color = primaryColor)
                }
            }
            Text(
                text = description,
                fontSize = 14.sp,
                color = secondaryColor
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    backgroundColor: Color,
    contentColor: Color
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(contentColor = contentColor),
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp, horizontal = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = "$text Icon",
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = text, fontSize = 16.sp, color = contentColor)
        }
    }
}
