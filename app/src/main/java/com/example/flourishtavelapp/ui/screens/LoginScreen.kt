package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGuideLoginSuccess: () -> Unit = {},
    onRegisterClick: () -> Unit,
    onBack: () -> Unit = {}
) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NatureGreenBackground) // Clean light nature green background
    ) {
        // ── 1. Top Left Back Button (Completely new, added per user request) ─────
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = DarkTextColor,
                modifier = Modifier.size(28.dp)
            )
        }

        // ── 2. Scrollable Content ─────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Sprouts/Leaf Logo inside a white circular badge with a green border
            Surface(
                modifier = Modifier.size(96.dp),
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(2.dp, Color(0xFF81C784)), // Soft Green Border
                shadowElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "N",
                            color = Color(0xFF388E3C),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Icon(
                            imageVector = Icons.Default.Eco, // Leaf sprout
                            contentDescription = "Logo sprout",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title "Đăng Nhập"
            Text(
                text = "Đăng Nhập",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle "Chào mừng bạn trở lại!"
            Text(
                text = "Chào mừng bạn trở lại!",
                color = Color(0xFF64748B),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // White Card Container
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Email input field
                    LoginInputField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        placeholder = "Email",
                        icon = Icons.Outlined.Email
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password input field
                    LoginInputField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        placeholder = "Mật khẩu",
                        icon = Icons.Outlined.Lock,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onPasswordToggle = { passwordVisible = !passwordVisible }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quên mật khẩu? text on the right
                    Text(
                        text = "Quên mật khẩu?",
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { },
                        color = Color(0xFF005b41),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── Button 1: Đăng nhập (Passenger) ───────────────────
                    Button(
                        onClick = {
                            val targetUsername = if (emailInput.contains("guide") || emailInput == "guide123") "guide123" else "bao123"
                            if (targetUsername == "guide123") {
                                onGuideLoginSuccess()
                            } else {
                                onLoginSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Phone, // Phone/Receiver icon matching image
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Đăng nhập",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }

                    TextDivider(text = "hoặc")

                    // ── Button 2: Đăng nhập với tư cách Hướng dẫn viên ──────
                    OutlinedButton(
                        onClick = onGuideLoginSuccess,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.2.dp, Color(0xFF005b41)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF005b41))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = Color(0xFF005b41),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Đăng nhập với tư cách",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Hướng dẫn viên",
                                    fontSize = 15.sp,
                                    color = Color(0xFF005b41),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    TextDivider(text = "Đăng nhập nhanh")

                    // Social login circles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circle 1: Google
                        SocialLoginCircle {
                            Text(
                                text = "G",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = Color(0xFFEA4335) // Google Red
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        // Circle 2: Black Leaf (Apple Sprout Equivalent)
                        SocialLoginCircle {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = "Apple Sprout",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        // Circle 3: Star Medal
                        SocialLoginCircle {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = "Medal Badge",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Footer text
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chưa có tài khoản? ",
                            color = Color(0xFF64748B),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Đăng ký",
                            color = Color(0xFF005b41),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onRegisterClick() }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Sub-Composables ───────────────────────────────────────────────────────────

@Composable
private fun LoginInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp)),
        placeholder = { Text(placeholder, color = Color(0xFF94A3B8), fontSize = 15.sp) },
        leadingIcon = { 
            Icon(
                imageVector = icon, 
                contentDescription = null, 
                tint = Color(0xFF475569), 
                modifier = Modifier.size(22.dp)
            ) 
        },
        trailingIcon = if (isPassword && onPasswordToggle != null) {
            {
                IconButton(onClick = onPasswordToggle) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility",
                        tint = Color(0xFF94A3B8)
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) androidx.compose.ui.text.input.PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF8FAFC),
            unfocusedContainerColor = Color(0xFFF8FAFC),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color(0xFF005b41)
        ),
        singleLine = true
    )
}

@Composable
private fun TextDivider(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0), thickness = 1.dp)
        Text(
            text = text,
            color = Color(0xFF94A3B8),
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0), thickness = 1.dp)
    }
}

@Composable
private fun SocialLoginCircle(
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(52.dp),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFCBD5E1))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}
