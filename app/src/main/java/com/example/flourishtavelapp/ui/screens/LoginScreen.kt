package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = DarkTextColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // Logo and Brand
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = PrimaryGreen
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Waves, null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                "Flourish",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = DarkTextColor
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Title
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Chào mừng trở lại!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = DarkTextColor
            )
            Text(
                "Đăng nhập để tiếp tục.",
                style = MaterialTheme.typography.bodyLarge,
                color = SecondaryTextColor
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Login Form Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                RegisterField(
                    label = "TÀI KHOẢN",
                    value = username,
                    onValueChange = { 
                        username = it
                        showError = false
                    },
                    placeholder = "Khách: bao123 | Guide: guide123",
                    icon = Icons.Default.Person
                )
                
                Spacer(modifier = Modifier.height(20.dp))

                RegisterField(
                    label = "MẬT KHẨU",
                    value = password,
                    onValueChange = { 
                        password = it
                        showError = false
                    },
                    placeholder = "Nhập mật khẩu (bao123)",
                    icon = Icons.Default.Lock,
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onPasswordToggle = { passwordVisible = !passwordVisible }
                )

                if (showError) {
                    Text(
                        text = "Tài khoản hoặc mật khẩu không đúng!",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    "Quên mật khẩu?",
                    modifier = Modifier.align(Alignment.End),
                    color = PrimaryGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Login Button
                Button(
                    onClick = {
                        when {
                            username == "guide123" && password == "guide123" -> {
                                onGuideLoginSuccess()
                            }
                            username == "bao123" && password == "bao123" -> {
                                onLoginSuccess()
                            }
                            else -> {
                                showError = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Đăng nhập", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Chưa có tài khoản? ", color = SecondaryTextColor, fontSize = 14.sp)
            Text(
                "Đăng ký ngay",
                color = PrimaryGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}
