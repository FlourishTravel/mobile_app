package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

@Composable
fun GroupCommunicationScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkTextColor)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Group communication",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp)
            )
        }
        Text(
            text = "Kênh đoàn có kiểm soát để tránh spam nhưng vẫn cập nhật nhanh",
            color = SecondaryTextColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 48.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LightGreenBackground.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Campaign,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Leader / guide ưu tiên phát thông báo",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextColor
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tin nhắn nhóm theo tour",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Khách tour nhận thông báo theo thời gian thực. Tính năng gửi ảnh đoàn có thể được leader mở theo từng chặng.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryTextColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Messages
        CommunicationMessageCard(
            name = "Minh Quân",
            role = "Tour leader",
            time = "08:10",
            message = "Mình đã ghim lại điểm tập trung và route BTS ở tab Khám phá. Mọi người kiểm tra trước khi ra ngoài nhé."
        )
        Spacer(modifier = Modifier.height(12.dp))
        CommunicationMessageCard(
            name = "Lan Hương",
            role = "Hướng dẫn viên",
            time = "11:45",
            message = "Chiều nay khu workshop hơi nóng, ưu tiên áo mỏng và mang theo nước cá nhân."
        )
        Spacer(modifier = Modifier.height(12.dp))
        CommunicationMessageCard(
            name = "Điều hành Flourish",
            role = "Điều hành tour",
            time = "12:00",
            message = "Nếu ai cần hỗ trợ y tế hoặc thất lạc giấy tờ, vào ngay tab Hỗ trợ để gọi hotline khẩn cấp 24/7."
        )
        Spacer(modifier = Modifier.height(12.dp))
        CommunicationMessageCard(
            name = "Album đoàn",
            role = "Thông báo cho khách",
            time = "12:05",
            message = "Tính năng gửi ảnh đoàn sẽ được leader mở theo từng chặng để tránh spam trong group chat."
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Actions Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Tác vụ nhanh",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                CommunicationQuickAction(
                    icon = Icons.Outlined.Call,
                    label = "Gọi leader ngay",
                    containerColor = LightGreenBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                CommunicationQuickAction(
                    icon = Icons.Outlined.Emergency,
                    label = "Mở emergency & support",
                    containerColor = LightGreenBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                CommunicationQuickAction(
                    icon = Icons.Outlined.Image,
                    label = "Upload ảnh đoàn (leader mở theo chặng)",
                    containerColor = Color(0xFFF0F7FF)
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun CommunicationMessageCard(name: String, role: String, time: String, message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = role, color = PrimaryGreen, style = MaterialTheme.typography.labelSmall)
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF5F5F5)
                ) {
                    Text(
                        text = time,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                color = DarkTextColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun CommunicationQuickAction(icon: ImageVector, label: String, containerColor: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = DarkTextColor
            )
        }
    }
}
