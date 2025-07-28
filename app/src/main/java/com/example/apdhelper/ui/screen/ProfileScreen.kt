package com.example.apdhelper.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apdhelper.R
import com.example.apdhelper.ui.components.profile.AccountActionButton
import com.example.apdhelper.ui.components.profile.GradientCardSection
import com.example.apdhelper.ui.components.profile.ProfileHeader
import com.example.apdhelper.ui.components.profile.StatisticItem
import com.example.apdhelper.ui.theme.Primary
import com.example.apdhelper.ui.theme.themedGradientBrush
import com.example.apdhelper.viewmodel.ProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val colors = MaterialTheme.colorScheme

    val achievements = remember(state.testCount, state.noteCount, state.streak) {
        listOfNotNull(
            if (state.streak >= 7) Triple(
                "🏆", "7-Day Streak", "Daily check-ins complete"
            ) else null,
            if (state.testCount >= 1) Triple(
                "🧠", "First Assessment", "Completed 1st test"
            ) else null,
            if (state.noteCount >= 10) Triple("📝", "Mindful Writer", "Logged 10+ notes") else null
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = colors.background) {
        if (state.loading) {
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
                ProfileHeader(state.name, state.email, state.memberSince)
                Spacer(modifier = Modifier.height(28.dp))

                GradientCardSection(title = "Your Statistics", gradient = themedGradientBrush()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatisticItem(state.testCount, "Tests", true, colors.primary)
                        StatisticItem(state.noteCount, "Notes", false, colors.primary)
                        StatisticItem(state.streak, "Streak", false, colors.primary)
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
                    AccountActionButton("Settings", Icons.Outlined.Settings) {
                        navController.navigate("settings")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    AccountActionButton("Help and Support", Icons.Outlined.Help) {
                        navController.navigate("help")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    AccountActionButton("About This App 🌙", Icons.Outlined.Info) {
                        navController.navigate("about")
                    }

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
