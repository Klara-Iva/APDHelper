package com.example.apdhelper

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlin.system.exitProcess
import com.example.apdhelper.ui.theme.*


@Composable
fun StartScreen(navController: NavController) {

    val signIn = LocalGoogleSignInLauncher.current

    BackHandler {
        exitProcess(0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .size(80.dp)
                    .background(
                        color = Primary,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Heart Outline",
                    tint = IconTintPrimary,
                    modifier = Modifier.size(50.dp)
                )
            }

            Text(
                text = "Welcome to APDHelper \uD83C\uDF19",
                fontSize = 22.sp,
                color = Primary
            )

            Text(
                text = "Your safe haven for managing anxiety and finding inner peace. We're here to support you on your wellness journey. ✨",
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 20.sp,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 32.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 4
            )

            Text(
                text = "Let’s get started! 🚀",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Choose how you'd like to join our supportive community",
                fontSize = 14.sp,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 11.dp),
                textAlign = TextAlign.Center,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Surface,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Button(
                        onClick = {
                            signIn()
                        },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 12.dp
                        ),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_email_24),
                                contentDescription = "Email Icon",
                                tint = Background,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                "Continue with Google ⚡",
                                fontSize = 15.sp,
                                color = Background
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Divider(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp),
                            color = DividerColor
                        )
                        Text(
                            text = "or",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = TextWhite,
                            fontSize = 14.sp
                        )
                        Divider(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp),
                            color = DividerColor
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = { navController.navigate("login") },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 12.dp
                        ),
                        colors = ButtonDefaults.buttonColors(containerColor = Surface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .border(
                                width = 1.dp,
                                color = Secondary,
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_person_24),
                                contentDescription = "person Icon",
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                "Sign In to Your Account 🔐",
                                fontSize = 15.sp,
                                color = Primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { navController.navigate("register") },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 12.dp
                        ),
                        colors = ButtonDefaults.buttonColors(containerColor = Surface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .border(
                                width = 1.dp,
                                color = Secondary,
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_person_add_alt_1_24),
                                contentDescription = "person add Icon",
                                tint = TextWhite,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text("Create New Account 🌱", fontSize = 15.sp, color = TextWhite)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Secondary,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_heart_outline),
                                    contentDescription = "person add Icon",
                                    tint = Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(24.dp))

                                Column {
                                    Text(
                                        text = "You're not alone",
                                        fontSize = 14.sp,
                                        color = TextWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Join thousands who’ve found comfort, tools, and community in their mental wellness journey. Your privacy and well-being are our top priorities.  \uD83D\uDC9C",
                                        fontSize = 13.sp,
                                        color = MutedText,
                                        lineHeight = 18.sp,
                                        modifier = Modifier.padding(top = 6.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Taking this step shows incredible courage and self-care 🌞",
                fontSize = 10.sp,
                color = MutedText
            )

            Text(
                text = "🧘‍♀️ 🦋 🌙 💜",
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
