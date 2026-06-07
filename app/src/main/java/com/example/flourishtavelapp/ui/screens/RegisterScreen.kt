package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*
import com.example.flourishtavelapp.data.api.RetrofitClient
import com.example.flourishtavelapp.data.model.RegisterRequest
import com.example.flourishtavelapp.data.session.SessionManager
import com.example.flourishtavelapp.data.model.UserInfo
import kotlinx.coroutines.launch

import androidx.activity.compose.BackHandler

@Composable
fun RegisterScreen(
    onRegisterSuccess: (UserInfo) -> Unit,
    onLoginClick: () -> Unit,
    onBack: () -> Unit = {}
) {
    BackHandler(onBack = onBack)
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val coroutineScope = rememberCoroutineScope()

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

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Bắt đầu hành trình",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = DarkTextColor
            )
            Text(
                "xanh của bạn.",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Registration Form Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Display error message if not null
                if (errorMessage != null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEE2E2), // Light red background
                        border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMessage!!,
                                color = Color(0xFF991B1B),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                RegisterField(
                    label = "HỌ TÊN",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Nguyễn Văn A",
                    icon = Icons.Default.Person
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                RegisterField(
                    label = "EMAIL",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "example@flourish.com",
                    icon = Icons.Default.AlternateEmail
                )

                Spacer(modifier = Modifier.height(20.dp))

                RegisterField(
                    label = "MẬT KHẨU",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "••••••••",
                    icon = Icons.Default.Lock,
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onPasswordToggle = { passwordVisible = !passwordVisible }
                )

                Spacer(modifier = Modifier.height(20.dp))

                RegisterField(
                    label = "NHẬP LẠI MẬT KHẨU",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "••••••••",
                    icon = Icons.Default.Lock,
                    isPassword = true,
                    passwordVisible = false // Confirm usually doesn't need toggle
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Terms and Conditions
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = agreeToTerms,
                        onCheckedChange = { agreeToTerms = it },
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen)
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Tôi đồng ý với ")
                            withStyle(style = SpanStyle(color = PrimaryGreen)) {
                                append("Điều khoản sử dụng")
                            }
                            append(" và ")
                            withStyle(style = SpanStyle(color = PrimaryGreen)) {
                                append("Chính sách bảo mật")
                            }
                            append(" của Flourish.")
                        },
                        fontSize = 11.sp,
                        color = SecondaryTextColor,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Register Button
                Button(
                    onClick = {
                        if (name.isBlank() || email.isBlank() || password.isBlank()) {
                            errorMessage = "Vui lòng nhập đầy đủ thông tin!"
                            return@Button
                        }
                        if (password != confirmPassword) {
                            errorMessage = "Mật khẩu nhập lại không khớp!"
                            return@Button
                        }
                        if (password.length < 6) {
                            errorMessage = "Mật khẩu phải chứa ít nhất 6 ký tự!"
                            return@Button
                        }
                        if (!agreeToTerms) {
                            errorMessage = "Bạn cần đồng ý với điều khoản sử dụng!"
                            return@Button
                        }
                        isLoading = true
                        errorMessage = null
                        coroutineScope.launch {
                            try {
                                val response = RetrofitClient.authApiService.register(
                                    RegisterRequest(
                                        fullName = name.trim(),
                                        email = email.trim(),
                                        password = password
                                    )
                                )
                                isLoading = false
                                if (response.isSuccessful && response.body()?.success == true) {
                                    val apiResponse = response.body()!!
                                    val authData = apiResponse.data
                                    if (authData != null) {
                                        sessionManager.saveSession(
                                            authData.accessToken,
                                            authData.refreshToken,
                                            authData.user
                                        )
                                        onRegisterSuccess(authData.user)
                                    } else {
                                        errorMessage = "Đăng ký thành công nhưng không nhận được dữ liệu từ server!"
                                    }
                                } else {
                                    errorMessage = response.body()?.message ?: "Đăng ký thất bại. Email có thể đã tồn tại!"
                                }
                            } catch (e: Exception) {
                                isLoading = false
                                errorMessage = "Lỗi kết nối đến máy chủ: ${e.localizedMessage}"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Đăng ký ngay", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Social Login Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
                    Text(
                        "HOẶC ĐĂNG KÝ VỚI",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontSize = 10.sp,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SocialButton(
                        modifier = Modifier.weight(1f),
                        iconRes = null, // Placeholder
                        label = "Google",
                        icon = Icons.Default.GTranslate
                    )
                    SocialButton(
                        modifier = Modifier.weight(1f),
                        iconRes = null,
                        label = "Facebook",
                        icon = Icons.Default.Facebook,
                        iconColor = Color(0xFF1877F2)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Đã có tài khoản? ", color = SecondaryTextColor, fontSize = 14.sp)
            Text(
                "Đăng nhập ngay",
                color = PrimaryGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null
) {
    Column {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.LightGray) },
            leadingIcon = { Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) },
            trailingIcon = if (isPassword && onPasswordToggle != null) {
                {
                    IconButton(onClick = onPasswordToggle) {
                        Icon(
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            null,
                            tint = Color.Gray
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF1F4F2),
                unfocusedContainerColor = Color(0xFFF1F4F2),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun SocialButton(modifier: Modifier, iconRes: Int?, label: String, icon: ImageVector, iconColor: Color = Color.Unspecified) {
    Surface(
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF1F4F2)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}
