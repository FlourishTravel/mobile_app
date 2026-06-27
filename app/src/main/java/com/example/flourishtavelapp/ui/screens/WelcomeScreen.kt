package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Emergency
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.ui.components.FeatureCard
import com.example.flourishtravelapp.ui.components.LoginTextField
import com.example.flourishtravelapp.ui.theme.*

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier, 
    onLoginSuccess: () -> Unit,
    onBuyTourClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Top Header Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TravelExplore,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Flourish Travel",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.5f)
            ) {
                Text(
                    text = "Internal Tour Access",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = SecondaryTextColor
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Flourish Mobile",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 36.sp,
                color = DarkTextColor
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Trợ lý du lịch số nội bộ cho khách hàng đã mua tour Flourish.",
            style = MaterialTheme.typography.bodyLarge,
            color = SecondaryTextColor
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Feature Cards
        FeatureCard(
            icon = Icons.Outlined.ConfirmationNumber,
            title = "Tour-only",
            description = "Mỗi đoàn có Tour Code riêng"
        )
        Spacer(modifier = Modifier.height(16.dp))
        FeatureCard(
            icon = Icons.Outlined.NotificationsActive,
            title = "Realtime",
            description = "Lịch trình & nhắc lịch cập nhật tức thời"
        )
        Spacer(modifier = Modifier.height(16.dp))
        FeatureCard(
            icon = Icons.Outlined.Emergency,
            title = "Emergency",
            description = "Leader, HDV, bệnh viện, đại sứ quán"
        )

        Spacer(modifier = Modifier.height(40.dp))

        LoginSection(onLoginSuccess, onBuyTourClick)
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun LoginSection(onLoginSuccess: () -> Unit, onBuyTourClick: () -> Unit) {
    // MOCK DATA
    var email by remember { mutableStateOf("guest@flourish.travel") }
    var password by remember { mutableStateOf("123456") }
    var tourCode by remember { mutableStateOf("FLR-BANGKOK-2026") }
    var showError by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = CardBackground
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Đăng nhập & mở hành trình",
                    style = MaterialTheme.typography.titleLarge,
                    color = DarkTextColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nhập tài khoản do Flourish cấp và Tour Code để xem nội dung đoàn của bạn.",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryTextColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            LoginTextField(label = "Email nội bộ / khách tour", value = email, onValueChange = { email = it })
            Spacer(modifier = Modifier.height(16.dp))
            LoginTextField(label = "Mật khẩu", value = password, onValueChange = { password = it }, isPassword = true)
            Spacer(modifier = Modifier.height(16.dp))
            LoginTextField(label = "Tour Code", value = tourCode, onValueChange = { tourCode = it })

            // Add Buy Tour Button below Tour Code
            Text(
                text = "Nếu bạn chưa có Tour Code? Mua ngay tại đây",
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable { onBuyTourClick() },
                color = PrimaryTeal,
                style = MaterialTheme.typography.bodySmall.copy(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                )
            )

            if (showError) {
                Text(
                    text = "Thông tin đăng nhập không chính xác!",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = LightGreenBackground
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircleOutline,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Preview hiện đang mở sẵn dữ liệu demo với Tour Code FLR-BANGKOK-2026.",
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email == "guest@flourish.travel" && password == "123456" && tourCode == "FLR-BANGKOK-2026") {
                        onLoginSuccess()
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(
                    text = "Mở Flourish Travel",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
