package com.example.apdhelper

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.apdhelper.bottomnavigationbar.BottomNavigationItems
import com.example.apdhelper.ui.theme.MutedText

@Composable
fun QuestionnaireScreen(navController: NavController) {
    val tests = listOf(
        Test(
            "GAD-7 Anxiety Scale",
            "Generalized Anxiety Disorder assessment",
            "5 min",
            "3 days ago",
            8
        ),
        Test("PHQ-9 Depression Scale", "Depression severity assessment", "3 min", "1 week ago", 5),
        Test(
            "Panic Disorder Severity",
            "Assess panic attack frequency and impact",
            "7 min",
            "2 days ago",
            12
        )
    )

    val recentResults = listOf(
        Result("Jan 18", "GAD-7", 8, "down"),
        Result("Jan 15", "Panic Scale", 12, "up"),
        Result("Jan 12", "PHQ-9", 5, "stable")
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),


        ) {


        Text(
            "Mental Health Assessments",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Track your progress with professional assessments designed to support your wellness journey ✨\n",
            color = MutedText,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )



        Box(
            modifier = Modifier

                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)

            ) {
                Row(modifier = Modifier.padding(10.dp)) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(16.dp)
                            ), contentAlignment = Alignment.Center
                    ) {
                        Text("🧠", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "Comprehensive Panic Disorder Assessment \uD83D\uDEA8",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            "Our most thorough assessment covering anxiety symptoms, triggers, coping mechanisms, and lifestyle factors to provide personalized insights.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            style = TextStyle(
                                lineHeight = 18.sp
                            ),

                            )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))


                val boxHeight = 100.dp

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .height(boxHeight)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Duration Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = "10–15 min",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary,
                                lineHeight = 14.sp,
                            )

                            Text(
                                text = "Duration",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 12.sp,
                            )
                        }
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .height(boxHeight)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Categories Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = "14 categories",
                                fontSize = 13.sp,
                                lineHeight = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Comprehensive",
                                fontSize = 11.sp,
                                lineHeight = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .height(boxHeight)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEmotions,
                                contentDescription = "Personalized Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                lineHeight = 14.sp,
                                text = "Personalized",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                lineHeight = 12.sp,
                                text = "Results",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }



                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {

                        navController.navigate("questions") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true

                        }
                    }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Start Comprehensive Assessment 🚀",
                        color = MaterialTheme.colorScheme.background
                    )
                }

                Text(
                    "This assessment helps identify patterns and provides tailored recommendations for your wellness journey \uD83D\uDC9C",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        lineHeight = 16.sp
                    ),
                )
            }
        }
    }
}

data class Test(
    val title: String,
    val description: String,
    val duration: String,
    val lastTaken: String,
    val score: Int
)

data class Result(
    val date: String, val test: String, val score: Int, val trend: String
)



