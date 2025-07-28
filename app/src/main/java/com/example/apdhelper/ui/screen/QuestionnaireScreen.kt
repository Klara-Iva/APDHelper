package com.example.apdhelper.ui.screen.questionnaire

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.apdhelper.ui.theme.MutedText

@Composable
fun QuestionnaireScreen(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
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
            Column(modifier = Modifier.padding(16.dp)) {
                AssessmentInfoCard()
                Spacer(modifier = Modifier.height(20.dp))
                AssessmentDetailsRow()
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
                    "This assessment helps identify patterns and provides tailored recommendations for your wellness journey 💜",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle(lineHeight = 16.sp),
                )
            }
        }
    }
}

@Composable
fun AssessmentInfoCard() {
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
                "Comprehensive Panic Disorder Assessment 🚨",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                "Our most thorough assessment covering anxiety symptoms, triggers, coping mechanisms, and lifestyle factors to provide personalized insights.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                style = TextStyle(lineHeight = 18.sp),
            )
        }
    }
}

@Composable
fun AssessmentDetailsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DetailBox(
            icon = Icons.Default.AccessTime,
            title = "10–15 min",
            subtitle = "Duration",
            modifier = Modifier.weight(1f)
        )
        DetailBox(
            icon = Icons.Default.List,
            title = "14 categories",
            subtitle = "Comprehensive",
            modifier = Modifier.weight(1f)
        )
        DetailBox(
            icon = Icons.Default.EmojiEmotions,
            title = "Personalized",
            subtitle = "Results",
            modifier = Modifier.weight(1f)
        )
    }

}

@Composable
fun DetailBox(
    icon: ImageVector, title: String, subtitle: String, modifier: Modifier = Modifier
) {
    val boxHeight = 100.dp
    Box(
        contentAlignment = Alignment.Center, modifier = modifier // <-- koristi vanjski modifier
            .height(boxHeight)
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), RoundedCornerShape(8.dp)
            )
            .padding(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
