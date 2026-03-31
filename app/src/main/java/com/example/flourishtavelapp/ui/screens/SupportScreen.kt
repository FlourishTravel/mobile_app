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
fun SupportScreen(modifier: Modifier = Modifier, onBack: () -> Unit, onLogout: () -> Unit) {
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
                text = "Emergency & support",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp)
            )
        }
        Text(
            text = "Leader, HDV, đại sứ quán, bệnh viện và checklist an toàn",
            color = SecondaryTextColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 48.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Emergency Red Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryGreen)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.Phone, null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("24/7 Emergency line", color = Color.White, style = MaterialTheme.typography.labelSmall)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Nếu có sự cố, gọi trước — xử lý sau",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Flourish ưu tiên hỗ trợ nhanh khi khách bị lạc đoàn, mất giấy tờ, gặp vấn đề sức khỏe hoặc cần đổi điểm hẹn.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { /* Call hotline */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
                ) {
                    Text("Gọi hotline +84 909 686 868", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Danh bạ hỗ trợ", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Liên hệ đã được ưu tiên cho tour hiện tại", color = SecondaryTextColor, style = MaterialTheme.typography.bodySmall)
        
        Spacer(modifier = Modifier.height(16.dp))

        SupportContactCard(
            title = "Leader đoàn • Minh Quân",
            description = "Điều phối chính, check-in, xử lý phát sinh tại điểm tập trung.",
            tag = "Leader",
            tagIcon = Icons.Outlined.Badge,
            phoneNumber = "+84 905 118 212"
        )
        Spacer(modifier = Modifier.height(16.dp))
        SupportContactCard(
            title = "HDV địa phương • Lan Hương",
            description = "Hỗ trợ di chuyển, dịch thuật cơ bản và xử lý tại Bangkok.",
            tag = "Guide",
            tagIcon = Icons.Outlined.PersonSearch,
            phoneNumber = "+66 81 234 1998"
        )
        Spacer(modifier = Modifier.height(16.dp))
        SupportContactCard(
            title = "Embassy of Vietnam in Thailand",
            description = "Liên hệ khi mất hộ chiếu, giấy tờ hoặc cần xác minh thông tin khẩn.",
            tag = "Embassy",
            tagIcon = Icons.Outlined.AccountBalance,
            address = "35 Wireless Road, Lumpini, Pathum Wan, Bangkok",
            phoneNumber = "+66 2 251 3552"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text("Checklist an toàn", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Thông tin quan trọng cho người lần đầu đi nước ngoài", color = SecondaryTextColor, style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))
        ChecklistItem(Icons.Outlined.Description, "Hộ chiếu & bản photo", "Bản chính cất kỹ trong pouch, ảnh chụp và bản photo luôn sẵn trên máy.")
        ChecklistItem(Icons.Outlined.Payments, "Tiền mặt nhỏ & thẻ", "Giữ khoảng 300–500 THB tiền lẻ để mua vé nhanh, nước uống và đi tàu.")
        ChecklistItem(Icons.Outlined.SettingsInputAntenna, "SIM / roaming hoạt động", "Luôn giữ ít nhất một kết nối dữ liệu để nhận thông báo tập trung theo thời gian thực.")
        ChecklistItem(Icons.Outlined.BatteryChargingFull, "Pin điện thoại trên 60%", "Trước giờ free time, kiểm tra pin và mang theo power bank nếu ra ngoài buổi tối.")

        Spacer(modifier = Modifier.height(32.dp))

        // Session Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Thông tin truy cập hiện tại", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(8.dp), color = LightGreenBackground) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.ConfirmationNumber, null, tint = PrimaryGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("FLR-BANGKOK-2026", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFF0F7FF)) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Email, null, tint = Color(0xFF42A5F5), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("guest@flourish.travel", style = MaterialTheme.typography.labelSmall)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(onClick = onLogout) {
                    Text("Đăng xuất bản demo", color = PrimaryGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun SupportContactCard(title: String, description: String, tag: String, tagIcon: ImageVector, phoneNumber: String, address: String? = null) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, color = SecondaryTextColor, style = MaterialTheme.typography.bodySmall)
            
            Spacer(modifier = Modifier.height(12.dp))
            Surface(shape = RoundedCornerShape(8.dp), color = LightGreenBackground) {
                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(tagIcon, null, tint = PrimaryGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(tag, color = PrimaryGreen, style = MaterialTheme.typography.labelSmall)
                }
            }

            if (address != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(address, color = PrimaryGreen, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Call */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkTextColor)
            ) {
                Text("Call $phoneNumber", color = Color.White)
            }
        }
    }
}

@Composable
fun ChecklistItem(icon: ImageVector, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(32.dp).background(LightGreenBackground, CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = PrimaryGreen, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(description, color = SecondaryTextColor, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
