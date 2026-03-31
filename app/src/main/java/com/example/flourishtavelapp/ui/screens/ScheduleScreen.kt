package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier, 
    onBack: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkTextColor
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("Chuyến đi", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkTextColor)
            }
            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onProfileClick() },
                shape = CircleShape,
                color = PrimaryGreen
            ) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(8.dp))
            }
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("YOUR JOURNEY", color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("My Trips", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = DarkTextColor)
            Text(
                "Lịch Trình Chi Tiết",
                color = SecondaryTextColor,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main Trip Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(32.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = com.example.flourishtavelapp.R.drawable.thailan),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(Color.Red, CircleShape))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ONGOING JOURNEY", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Khám Phá Thái Lan Chill", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Timeline Starts
            TimelineItem(
                day = "D1",
                title = "TP.HCM – BANGKOK – PATTAYA",
                status = "COMPLETED",
                description = "Trưa: Đáp chuyến bay đến Bangkok, xe đón đoàn đi thẳng về Pattaya.\nChiều: Check-in khách sạn, sau đó đi thẳng đến The Sky Gallery hoặc Tutu Beach.\nTrải nghiệm: Ngồi ghế lười ngắm hoàng hôn, uống một ly nước dừa mát lạnh và nghe tiếng sóng.",
                isCompleted = true,
                isCurrent = false
            )

            TimelineItem(
                day = "D2",
                title = "ĐẢO CORAL – VỀ LẠI BANGKOK",
                status = "GPS ACTIVE",
                description = "Sáng: Đi cano ra đảo Coral. Hãy dành thời gian nằm dưới tán cây đọc sách hoặc bơi lội tự do.\nTrưa: Ăn trưa hải sản tươi sống ngay tại đảo.\nTối: Check-in khách sạn. Đoàn có thể tự do dạo quanh khu vực khách sạn hoặc ghé Rooftop Bar.",
                isCompleted = false,
                isCurrent = true,
                showActionCard = true
            )

            TimelineItem(
                day = "D3",
                title = "CHẬM RÃI GIỮA LÒNG BANGKOK",
                status = "AVAILABLE",
                description = "Sáng: Tham quan Wat Arun (2-3 tiếng thong thả chụp ảnh).\nTrưa: Ăn món Thái ven sông.\nChiều: Ghé The Commons (Thonglor) - không gian mở cực chill.",
                isCompleted = false,
                isCurrent = false,
                isLocked = false
            )
            
            TimelineItem(
                day = "D4",
                title = "NGHỆ THUẬT & TẦM NHÌN",
                status = "AVAILABLE",
                description = "Sáng: Bảo tàng MOCA.\nChiều: Lên sàn kính Mahanakhon Skywalk ngắm hoàng hôn và thành phố lên đèn.",
                isCompleted = false,
                isCurrent = false,
                isLocked = false
            )

            TimelineItem(
                day = "D5",
                title = "TẠM BIỆT THÁI LAN",
                status = "AVAILABLE",
                description = "Sáng: Tự do đi dạo công viên Lumpini hoặc ghé tiệm cafe yêu thích.\nTrưa: Xe đưa đoàn ra sân bay về lại TP.HCM.",
                isCompleted = false,
                isCurrent = false,
                isLocked = false,
                isLast = true
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun TimelineItem(
    day: String,
    title: String,
    status: String,
    description: String,
    isCompleted: Boolean = false,
    isCurrent: Boolean = false,
    isLocked: Boolean = false,
    isLast: Boolean = false,
    showActionCard: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Timeline Connector
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(40.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = when {
                            isCompleted -> PrimaryGreen
                            isCurrent -> PrimaryGreen
                            else -> Color.White
                        },
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = if (isLocked) Color.LightGray else PrimaryGreen,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(18.dp))
                } else if (isLocked) {
                    Text(day.replace("D", ""), color = Color.LightGray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                } else {
                    Text(
                        text = day.replace("D", ""), 
                        color = if (isCurrent || isCompleted) Color.White else PrimaryGreen, 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 12.sp
                    )
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(
                            if (isCompleted) PrimaryGreen else Color.LightGray,
                            shape = RoundedCornerShape(1.dp)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Content
        Column(modifier = Modifier.padding(bottom = 32.dp).weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isCurrent) {
                    Icon(Icons.Default.Navigation, null, tint = PrimaryGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = "DAY ${day.replace("D", "")} • $status",
                    color = if (isLocked) Color.LightGray else SecondaryTextColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                if (isCurrent) {
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        color = LightGreenBackground,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("TODAY", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = PrimaryGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isLocked) Color.LightGray else DarkTextColor
            )
            
            if (isLocked) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Outlined.Lock, null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("CHI TIẾT MỞ VÀO NGÀY MAI", color = Color.LightGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = description,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryTextColor,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            if (showActionCard) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = Color(0xFFB2EBF2)) {
                                Icon(Icons.Default.Person, null, tint = Color(0xFF00838F), modifier = Modifier.padding(12.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Minh Quân", fontWeight = FontWeight.Bold, color = DarkTextColor)
                                Text("Tour Leader", color = SecondaryTextColor, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = PrimaryGreen) {
                                Icon(Icons.Default.Call, null, tint = Color.White, modifier = Modifier.padding(10.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.Navigation, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Mở bản đồ lịch trình", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
