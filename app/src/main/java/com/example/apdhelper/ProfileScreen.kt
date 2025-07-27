package com.example.apdhelper

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apdhelper.ui.theme.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme


    var userName by remember { mutableStateOf<String?>(null) }
    var userEmail by remember { mutableStateOf<String?>(null) }
    var testCount by remember { mutableStateOf(0) }
    var noteCount by remember { mutableStateOf(0) }
    var streak by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }

    val uid = auth.currentUser?.uid
    val memberSince = FirebaseAuth.getInstance().currentUser?.metadata?.creationTimestamp?.let {
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(it))
    } ?: "Unknown"

    val achievements = remember(testCount, noteCount, streak) {
        listOfNotNull(
            if (streak >= 7) Triple("🏆", "7-Day Streak", "Daily check-ins complete") else null,
            if (testCount >= 1) Triple("🧠", "First Assessment", "Completed 1st test") else null,
            if (noteCount >= 10) Triple("📝", "Mindful Writer", "Logged 10+ notes") else null
        )
    }

    LaunchedEffect(Unit) {
        if (uid != null) {
            firestore.collection("users").document(uid).get().addOnSuccessListener { doc ->
                    userName = doc.getString("name")
                    userEmail = doc.getString("email")
                    loading = false
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to load profile.", Toast.LENGTH_SHORT).show()
                }

            firestore.collection("users").document(uid).collection("tests")
                .document("PanicDisorderTests").collection("entries").get()
                .addOnSuccessListener { result ->
                    testCount = result.size()
                }

            firestore.collection("users").document(uid).collection("notes").get()
                .addOnSuccessListener { noteCount = it.size() }

            firestore.collection("users").document(uid).collection("visits").get()
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
                    streak = count
                }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = colors.background) {
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colors.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileHeader(userName, userEmail, memberSince)
                Spacer(modifier = Modifier.height(28.dp))
                GradientCardSection(title = "Your Statistics", gradient = themedGradientBrush()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatisticItem(testCount, "Tests", true, colors.primary)
                        StatisticItem(noteCount, "Notes", false, colors.primary)
                        StatisticItem(streak, "Streak", false, colors.primary)
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
                if (achievements.isNotEmpty()) {
                    GradientCardSection(title = "Achievements", gradient = themedGradientBrush()) {
                        achievements.forEach {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(it.first, fontSize = 26.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        it.second,
                                        lineHeight = 16.sp,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colors.primary,
                                    )
                                    Text(
                                        it.third,
                                        lineHeight = 15.sp,
                                        fontSize = 13.sp,
                                        color = colors.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Primary.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        "Earned",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }




                Spacer(modifier = Modifier.height(28.dp))
                GradientCardSection(title = "Account", gradient = themedGradientBrush()) {
                    AccountActionButton("Settings and Privacy", Icons.Outlined.Settings)
                    Spacer(modifier = Modifier.height(12.dp))
                    AccountActionButton("Help & Support", Icons.Outlined.Help)
                    Spacer(modifier = Modifier.height(12.dp))


                    val googleSignInClient = GoogleSignIn.getClient(
                        context,
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(context.getString(R.string.default_web_client_id))
                            .requestEmail().build()
                    )
                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            googleSignInClient.signOut().addOnCompleteListener {
                                navController.navigate("start") {
                                    popUpTo("profile") { inclusive = true }
                                }
                            }
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B6B).copy(alpha = 0.6f),
                            contentColor = Color.White
                        ), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Logout, contentDescription = "Logout")
                            Spacer(Modifier.width(8.dp))
                            Text("Log Out")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GradientCardSection(
    title: String, gradient: Brush, content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient, RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(Modifier.height(14.dp))
        content()
    }
}

@Composable
fun ProfileHeader(name: String?, email: String?, since: String) {
    val initials =
        name?.split(" ")?.map { it.firstOrNull()?.uppercase() ?: "" }?.joinToString("") ?: "?"
    val colors = MaterialTheme.colorScheme

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    initials,
                    color = colors.onPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(colors.primary.copy(alpha = 0.7f))
                    .border(1.dp, colors.onPrimary, CircleShape)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    tint = colors.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(
            name ?: "Unknown",
            color = colors.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(6.dp))
        Text(email ?: "Unknown", color = colors.onSurface.copy(alpha = 0.6f), fontSize = 14.sp)
        Spacer(Modifier.height(6.dp))
        Text("Member since $since", color = colors.onSurface.copy(alpha = 0.6f), fontSize = 14.sp)
    }
}

@Composable
fun StatisticItem(count: Int, label: String, highlight: Boolean, color: Color) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(84.dp)
            .border(
                width = 1.dp, brush = themedGradientBrush(), shape = RoundedCornerShape(12.dp)
            )
            .background(
                if (highlight) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                RoundedCornerShape(12.dp)
            ), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$count", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 14.sp, color = TextPrimary, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun AchievementItem(icon: String, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 22.sp, modifier = Modifier.padding(end = 12.dp))
        Column {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Primary)
            Text(desc, fontSize = 13.sp, color = TextPrimary)
        }
    }
}

@Composable
fun AccountActionButton(text: String, icon: ImageVector) {
    Button(
        onClick = {}, shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.outline,
            contentColor = MaterialTheme.colorScheme.primary
        ), modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(text, fontSize = 15.sp)
        }
    }
}
