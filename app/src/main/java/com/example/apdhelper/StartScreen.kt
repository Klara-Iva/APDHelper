package com.example.apdhelper

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apdhelper.ui.theme.Lavander
import com.example.apdhelper.ui.theme.Purple40
import com.example.apdhelper.ui.theme.Purple80
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlin.system.exitProcess

private const val RC_SIGN_IN = 9001

@Composable
fun StartScreen(navController: NavController) {
    val context = LocalContext.current

    BackHandler {
        exitProcess(0)
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Lavander)
    ) {
        Column(
            modifier = Modifier

                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Dobrodošli!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Odaberite način prijave", fontSize = 16.sp, color = Color(0xFFB3A9A1)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("register") },
                colors = ButtonDefaults.buttonColors(containerColor = Purple80),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(50.dp)
                    .semantics {
                        contentDescription = "navigateToRegistrationButton"
                    }
            ) {
                Text("Kreiraj novi račun", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.buttonColors(containerColor = Purple80),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(50.dp)
                    .semantics {
                        contentDescription = "navigateToLoginButton"
                    }
            ) {
                Text("Prijavi se", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val signInIntent = googleSignInClient.signInIntent
                    (context as? Activity)?.startActivityForResult(signInIntent, RC_SIGN_IN)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Purple80),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(50.dp)
                    .semantics {
                        contentDescription = "loginWithGoogle"
                    }
            ) {
                Text("Prijava putem Google računa", fontSize = 14.sp, color = Color.White)
            }

        }
    }
}
