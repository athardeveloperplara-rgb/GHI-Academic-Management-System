package com.example.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HospCrimson
import com.example.ui.theme.HospEmerald
import com.example.ui.theme.HospGoldAccent
import com.example.ui.theme.HospGoldBright
import com.example.ui.theme.HospNavyDark
import com.example.ui.theme.HospNavyLight
import com.example.ui.theme.HospNavyPrimary

@Composable
fun LoginScreen(
    errorMessage: String?,
    onLogin: (username: String, password: String) -> Boolean,
    onQuickLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("admin") }
    var password by remember { mutableStateOf("grand123") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = HospNavyDark
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Institute Crest & Header
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(HospGoldAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Crest",
                        tint = HospNavyDark,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = HospGoldBright,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "GRAND HOSPITALITY",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        color = HospGoldBright
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "INSTITUTE OF HOTEL MANAGEMENT & CULINARY ARTS",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Sistem Administrasi Akademik & Pengelolaan Nilai",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = HospGoldAccent,
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Login Form Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 480.dp)
                        .testTag("login_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = HospNavyPrimary
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Portal Masuk Staf Administrasi",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Silakan masukkan username dan kata sandi",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        // Error Banner if any
                        if (errorMessage != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(HospCrimson.copy(alpha = 0.2f))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = errorMessage,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFFFFB4AB),
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Username input
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("username_input"),
                            label = { Text("Username") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = HospGoldAccent)
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = HospGoldAccent,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                                focusedLabelColor = HospGoldAccent,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Password input
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("password_input"),
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = HospGoldAccent)
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { isPasswordVisible = !isPasswordVisible },
                                    modifier = Modifier.testTag("toggle_password_visibility")
                                ) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password",
                                        tint = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { onLogin(username, password) }
                            ),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = HospGoldAccent,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                                focusedLabelColor = HospGoldAccent,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                            )
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        // Login Button
                        Button(
                            onClick = { onLogin(username, password) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("login_submit_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HospGoldAccent,
                                contentColor = HospNavyDark
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Key, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Masuk ke Dashboard",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Login Button for easy testing
                        OutlinedButton(
                            onClick = onQuickLogin,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("quick_login_demo_button"),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = HospGoldBright
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                width = 1.dp
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Masuk Instan (Demo Staf)", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Credentials hint card
                Row(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = HospGoldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Akses Staf: Username: admin | Kata Sandi: grand123",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.5.sp
                        )
                    )
                }
            }
        }
    }
}
