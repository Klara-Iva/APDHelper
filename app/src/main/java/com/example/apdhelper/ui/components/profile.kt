package com.example.apdhelper.ui.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apdhelper.ui.theme.TextPrimary
import com.example.apdhelper.ui.theme.themedGradientBrush

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
fun AccountActionButton(text: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick, shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(
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
